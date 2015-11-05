package com.alam.masuksekolah;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Dawaam on 06/11/2015.
 */
public class Shared extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_shared);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
