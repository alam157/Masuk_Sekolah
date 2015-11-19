package com.alam.masuksekolah;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Dawaam on 06/11/2015.
 */
public class Login extends AppCompatActivity{

    EditText edUser, edPass;
    Button buttonLogin;
    TextView btnRegister;

    SharedPreferences sharedPreferences;

    public static final String MYPREF = "data";
    public static final String USER_KEY = "user";
    public static final String STATUS = "status";

    Context context = this;

    List<NameValuePair> parameter = new ArrayList<NameValuePair>(2);

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        addListenerOnButton();

        edUser = (EditText) findViewById(R.id.input_user);
        edPass = (EditText) findViewById(R.id.input_pass);
        btnRegister = (TextView) findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

    }

    private void addListenerOnButton() {
        buttonLogin = (Button) findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                parameter.add(new BasicNameValuePair("username", edUser.getText().toString()));
                parameter.add(new BasicNameValuePair("password", edPass.getText().toString()));

                new DoLogin().execute();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class DoLogin extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setMessage("Login in...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ServiceHandler serviceHandler = new ServiceHandler();

            String jsonStr = serviceHandler.makeServiceCall("http://doroutdoor.com/hackathon/login.php", ServiceHandler.POST, parameter);

            Log.d("Response: ", "> " + jsonStr);

            String result = null;
            if (jsonStr != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    JSONObject object = jsonObject.getJSONObject("json");

                    result = object.getString("status");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Log.e("ServiceHandler", "Couldn't get any data from the url");}

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (result.equals("Success")){
                sharedPreferences = getSharedPreferences(MYPREF, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(USER_KEY, edUser.getText().toString());
                editor.putString(STATUS, "active");
                editor.commit();

                Intent intent = new Intent(context, Home.class);
                Login.this.finish();
                startActivity(intent);
            }else {
                Toast.makeText(Login.this, "Username atau Password Salah", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
