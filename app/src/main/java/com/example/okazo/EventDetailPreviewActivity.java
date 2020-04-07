package com.example.okazo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.IOException;
import java.util.ArrayList;

public class EventDetailPreviewActivity extends AppCompatActivity {
    AppBarLayout appBarLayout;
    AppCompatImageButton buttonConfirm;

    private ImageView imageView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private static final int CHOOSE_IMAGE = 505;
    private Uri uriProfileImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_preview);
        appBarLayout= findViewById(R.id.event_detail_preview_app_bar);
        buttonConfirm=findViewById(R.id.event_detail_preview_confirm);
        imageView=findViewById(R.id.event_detail_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        collapsingToolbarLayout=findViewById(R.id.event_detail_preview_collapsing_tool_bar);
       appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
           int scrollRange=-1;
           @Override
           public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
               if(scrollRange==-1){
                   scrollRange=appBarLayout.getTotalScrollRange();
               }

               if(scrollRange+i==0){

                   buttonConfirm.setVisibility(View.VISIBLE);
                   collapsingToolbarLayout.setTitle("Okazo");

               }else {
                   //fully expanded
                   buttonConfirm.setVisibility(View.GONE);
                   collapsingToolbarLayout.setTitle("");

               }
           }
       });
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select profile image"), CHOOSE_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            uriProfileImage = data.getData();


                Glide.with(EventDetailPreviewActivity.this)
                        .load(uriProfileImage)
                        .placeholder(R.drawable.ic_place_holder_background)
                        .error(R.drawable.ic_image_not_found_background)
                        .centerCrop()
                        .into(imageView);
                //imageView.setImageBitmap(bitmap);
               // handleUpload(bitmap);

        }
    }


}
