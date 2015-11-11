package com.alam.masuksekolah;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Config;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;

/**
 * Created by Dawaam on 06/11/2015.
 */
public class Shared extends AppCompatActivity{

    EditText location, nama, pendidikan, alasan, latitude, longtitude ;
    ImageView takePicture;

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    List<NameValuePair> data = new ArrayList<NameValuePair>(2);

    String encodedString;
    Bitmap bm;

    ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_shared);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        takePicture = (ImageView) findViewById(R.id.take_photo);
        nama = (EditText) findViewById(R.id.input_name);
        pendidikan = (EditText) findViewById(R.id.input_sekolah);
        alasan = (EditText) findViewById(R.id.input_alasan);
        latitude = (EditText) findViewById(R.id.latitude);
        longtitude = (EditText) findViewById(R.id.longtitude);


        location = (EditText) findViewById(R.id.input_lokasi);
        location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    location.clearFocus();
                    Intent intent = new Intent(Shared.this, Maps.class);
                    startActivity(intent);
                }
            }
        });

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            sendData();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendData(){
        data.add(new BasicNameValuePair("nama", nama.getText().toString()));
        data.add(new BasicNameValuePair("sekolah", pendidikan.getText().toString()));
        data.add(new BasicNameValuePair("alasan", alasan.getText().toString()));
        data.add(new BasicNameValuePair("lokasi", location.getText().toString()));
        data.add(new BasicNameValuePair("latitude", latitude.getText().toString()));
        data.add(new BasicNameValuePair("longtitude", longtitude.getText().toString()));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bm.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        encodedString = Base64.encodeBytes(byte_arr);

        data.add(new BasicNameValuePair("image", encodedString));
        Log.d("Encode :", encodedString);

        new SendData().execute();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null)
            setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if ( intent.getExtras() != null){
            Bundle bundle = intent.getExtras();
            String city = bundle.getString("loc");
            double myLat = bundle.getDouble("myLat");
            double myLong = bundle.getDouble("myLong");

            longtitude.setText(String.valueOf(myLong));
            latitude.setText(String.valueOf(myLat));
            location.setText(city);

            Log.d("myLat myLong ", "" + String.valueOf(myLat) + " " + String.valueOf(myLong));
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Shared.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra("loc", "Jakarta Masukkk");
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);}
            else if (requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);}
        }
    }

    private void onCaptureImageResult(Intent data) {
        bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        takePicture.setImageBitmap(bm);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        takePicture.setImageBitmap(bm);
    }

    private class SendData extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Shared.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

//             Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://doroutdoor.com/hackathon/insert_data.php", ServiceHandler.POST, data);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr == null){
                Log.e("ServiceHandler", "Couldn't get any data from the url");}

//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost("192.168.43.184/hackathon/insert_data.php");
//
//            try {
//
//                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
//                File sourceFile = new File(filePath);
//
//                // Adding file data to http body
//                entity.addPart("image", new FileBody(sourceFile));
//
//                // Extra parameters if you want to pass to server
//                entity.addPart("website",new StringBody("test"));
//                entity.addPart("email", new StringBody("abc@gmail.com"));
//
//                totalSize = entity.getContentLength();
//                httppost.setEntity(entity);
//
//                // Making server call
//                HttpResponse response = httpclient.execute(httppost);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (pDialog.isShowing())
                pDialog.dismiss();

            onBackPressed();
        }
    }
}
