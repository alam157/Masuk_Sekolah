package com.alam.masuksekolah;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by admin_tnss on 11/12/2015.
 */
public class Detail extends AppCompatActivity {

    TextView nama, sekolah, lokasi, alasan;
    double lat, lng;
    ImageView vImage;
    Bitmap image;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_card);

        nama = (TextView) findViewById(R.id.text_nama);
        sekolah = (TextView) findViewById(R.id.text_sekolah);
        lokasi = (TextView) findViewById(R.id.text_location);
        alasan = (TextView) findViewById(R.id.text_alasan);
        vImage = (ImageView) findViewById(R.id.ivAnak);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        byte[] byte_arr = bundle.getByteArray("image");
        image = BitmapFactory.decodeByteArray(byte_arr, 0, byte_arr.length);
        vImage.setImageBitmap(image);
        nama.setText(bundle.getString("nama"));
        sekolah.setText(bundle.getString("sekolah"));
        lokasi.setText(bundle.getString("lokasi"));
        alasan.setText(bundle.getString("alasan"));
        lat = bundle.getDouble("lat");
        lng = bundle.getDouble("lng");

        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat-0.001, lng-0.01))
                .zoom(14)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
    }
}
