package com.alam.masuksekolah;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by admin_tnss on 11/4/2015.
 */
public class ContactInfo {
    protected String name;
    protected String alasan;
//    protected Drawable image;
    protected Bitmap image;
    protected double lat;
    protected double lng;
    protected String sekolah;
    protected String lokasi;
    protected Boolean isValid;


    protected static final String NAME_PREFIX = "Nama ";
    protected static final String ALASAN_PREFIX = "Karena ";
}