package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CaptureMedication extends AppCompatActivity {

    //Initialising Variables
    ImageView imageView;
    Button buttonCapture,buttonGallery,buttonVGallery;
    BitmapDrawable drawable;
    Bitmap bitmap;
    int SELECT_PICTURE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_medication);

        //Assigning variables
        imageView = findViewById(R.id.imageView);
        buttonCapture = findViewById(R.id.buttonCapture);
        buttonGallery = findViewById(R.id.buttonGallery);
        buttonVGallery = findViewById(R.id.buttonVGallery);

        if (ContextCompat.checkSelfPermission(CaptureMedication.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CaptureMedication.this, new String[]{Manifest.permission.CAMERA}, 100);
        }

        buttonCapture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent captureImage = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(captureImage,100);
            }
        });


        //Permissions to store images
        ActivityCompat.requestPermissions(CaptureMedication.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    saveToGallery();
            }
        });
        //Gallery button
        buttonVGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSelectPic();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            //This captures the image
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //Assigning CaptureImagine to ImageView
            imageView.setImageBitmap(captureImage);
        }else if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_PICTURE) {
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        imageView.setImageURI(selectedImageUri);
                    }
                }
            }
    }


    //Method save to Gallery
    private void saveToGallery()  {
    //Getting the image from the ImageView and storing it
     drawable = (BitmapDrawable)imageView.getDrawable();
     bitmap = drawable.getBitmap();
     //Determines the path for saved pics
     FileOutputStream outputStream = null;
     File sdCard = Environment.getExternalStorageDirectory();
     File directory = new File (sdCard.getAbsolutePath()+"/Pictures");
     directory.mkdir();
     String fileName = String.format("%d.jpg",System.currentTimeMillis());
     File outFile = new File(directory,fileName);
        Toast.makeText(CaptureMedication.this,"You have successfully saved your image",Toast.LENGTH_SHORT).show();
     try {
         outputStream = new FileOutputStream(outFile);
         bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
         outputStream.flush();
         outputStream.close();
         //Refreshes gallery to update pic
         Intent pic = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
         pic.setData(Uri.fromFile(outFile));
         sendBroadcast(pic);

     }catch (FileNotFoundException e){
         e.printStackTrace();
     } catch (IOException e){
         e.printStackTrace();
     }
    }

    public void userSelectPic(){
        Intent select = new Intent();
        select.setType("image/*");
        select.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(select, "Select Picture"), SELECT_PICTURE);
    }





}

