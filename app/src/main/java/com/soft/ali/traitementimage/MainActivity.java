package com.soft.ali.traitementimage;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Img image;
    ImgView iv;
    Uri photoURI;
    File photoCaptured;
    Context mainContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainContext = this;
        iv = (ImgView) findViewById(R.id.iv);
        iv.setOnTouchListener(new ScrollZoomListener());

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contraste);
        image = new Img(bitmap);

        Button b = (Button) findViewById(R.id.ega);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgProcessing.setImage(image);

            }
        });

        Button bCam = (Button) findViewById(R.id.cam);
        bCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceHasCamera())
                    launchCamera();
                else
                    Toast.makeText(mainContext, "No camera available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method will be used when an activity returns.
     * It allows us to extract the data we need and use them later.
     * @param requestCode is used to know which activity has returned.
     * @param resultCode allows us to know if the actiity has ended well or not.
     * @param data the activity returns.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Capturing a photo via the camera doesn't return any data.
        if (requestCode == Constants.REQUEST_CAPTURE && resultCode == RESULT_OK ) {
            //Cleaning the RAM.
            image.clearMemory();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                image = new Img(bitmap);
                iv.setImageBitmap(image.getOriginalBitmap());
                //Deleting the photo when loaded in RAM.
                photoCaptured.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == Constants.REQUEST_CAPTURE && resultCode == RESULT_CANCELED){
            Toast.makeText(this, "Capture Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method checks if the device has any camera (front or back).
     * The verification is made by accessing the package manager which gives information about the various application packages.
     * As the phone camera is recognized as an app, checking if a camera is available is possible via the package manager.
     * @return true if the device has a camera, false instead.
     */
    private boolean deviceHasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    /**
     * This method launches the camera and allows the user to capture a picture.
     * An Intent is launched to start the camera.
     * The captured image is placed in a temporary file then in a URI.
     * A URI is needed to store the captured image.
     * the putExtra method tells to the camera that the image needs to be stored in this URI.
     * The data captured by the camera are stored in th URI and the result code is sent.
     */
    private void launchCamera(){
        Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoCaptured = createTemporaryFile();
        photoURI = Uri.fromFile(photoCaptured);

        cam.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(cam, Constants.REQUEST_CAPTURE);
    }

    /**
     * Create a temporary file in memory to store the photo.
     * The file is deleted when the user confirms the capture.
     * @return The path of the temporary file.
     */
    private File createTemporaryFile(){
        File temp = Environment.getExternalStorageDirectory();
        temp = new File(temp.getAbsolutePath() +"/.temp");
        if (!temp.exists()){
            temp.mkdir();
        }
        try {
            return File.createTempFile(Constants.FILE_NAME, Constants.EXTENSION, temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
