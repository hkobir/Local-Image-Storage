package com.hk.image_local_store;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hk.image_local_store.util.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton addBtn;
    AlertDialog dialog;
    private ImageView prdImage;
    int REQUEST_CHECK = 0;
    String imageUrl;
    File imgPath;
    private PhotoviewModel photoviewModel;
    private List<Photo> photoList;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.imageRV);
        photoList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));

        addBtn = findViewById(R.id.addFAB);
        photoviewModel = new ViewModelProvider(this).get(PhotoviewModel.class);

        addBtn.setOnClickListener(l -> showAddImageDialog());

        getPermission();
        populateData();

    }

    private void populateData() {
        photoviewModel.getAllPhoto().observe(this, photos -> {
            photoList = photos;
            imageAdapter = new ImageAdapter(MainActivity.this,photoList);
            recyclerView.setAdapter(imageAdapter);

        });

    }

    public void showAddImageDialog() {
        dialog = new AlertDialog.Builder(MainActivity.this).create();
        dialog.setMessage("Add Image");
        dialog.setCancelable(true);
        final View view = LayoutInflater.from(MainActivity.this).
                inflate(R.layout.add_image_dialog, null);
        prdImage = view.findViewById(R.id.productImageIV);
        Button uploadBtn = view.findViewById(R.id.uploadImageButton);
        Button submitBtn = view.findViewById(R.id.submitButton);
        uploadBtn.setOnClickListener(l -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fp = getFile(MainActivity.this);
            Uri imageUri = FileProvider.getUriForFile(
                    MainActivity.this,
                    "com.hk.image_local_store.provider", //(use your app signature + ".provider" )
                    fp);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CHECK);
        });
        submitBtn.setOnClickListener(l -> {
            Photo photo = new Photo("demo tittle " + new Random()
                    .nextInt(100) + 1, imageUrl);
            photoviewModel.insertPhoto(photo);
            dialog.dismiss();
            Toast.makeText(this, "photo inserted!", Toast.LENGTH_SHORT).show();
        });
        dialog.setView(view);
        dialog.show();

    }

    private File getFile(Context context) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/"
                + context.getResources().getString(R.string.app_name)
        );
        if (!folder.exists()) {
            folder.mkdir();
        }
        imgPath = new File(folder, File.separator +

                System.currentTimeMillis() + ".jpg");


        return imgPath;
    }

    public void getPermission() {
        String[] permissions =
                {Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(permissions, 0);//request for permission
            } else {
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (imgPath.exists()) {
            try {
                imageUrl = imgPath.toString();
                Toast.makeText(this, imageUrl, Toast.LENGTH_SHORT).show();
                prdImage.setImageBitmap(
                        Common.decodeSampledBitmap(MainActivity.this, imageUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            prdImage.setImageResource(R.drawable.insert_drive_file);
        }

    }

    /*
    for update and delete remove existing image
    if (new File(imageUrl).exists())
            new File(imageUrl).delete();

     */


}