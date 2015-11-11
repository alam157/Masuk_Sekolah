package com.alam.masuksekolah;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by admin_tnss on 11/4/2015.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<ContactInfo> contactList;
    private Context context;

    public ContactAdapter(List<ContactInfo> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        final ContactInfo ci = contactList.get(i);
        contactViewHolder.vName.setText(ci.name);
        contactViewHolder.vSurname.setText(ci.alasan);
        contactViewHolder.vImage.setImageBitmap(ci.image);
        contactViewHolder.vImage.setScaleType(ImageView.ScaleType.FIT_XY);

        contactViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                ci.image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byte_arr = stream.toByteArray();

                Bundle bundle = new Bundle();
                bundle.putString("nama", ci.name);
                bundle.putString("lokasi", ci.lokasi);
                bundle.putString("sekolah", ci.sekolah);
                bundle.putString("alasan", ci.alasan);
                bundle.putDouble("lat", ci.lat);
                bundle.putDouble("lng", ci.lng);
                bundle.putByteArray("image", byte_arr);
                Intent intent = new Intent(context, Detail.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_home, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vSurname;
        protected ImageView vImage;
        protected Button btnDetail;

        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vSurname = (TextView)  v.findViewById(R.id.txtSurname);
            vImage = (ImageView) v.findViewById(R.id.ivKonten);
            btnDetail = (Button) v.findViewById(R.id.btnDetail);
        }
    }
}
