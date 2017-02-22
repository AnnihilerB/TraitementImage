package com.soft.ali.traitementimage;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private File photoCaptured;
    private Uri photoURI;
    Intent cam;

    //ID needed for requesting a capture from the camera.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (deviceHasCamera()) {
            launchCamera();
            //setResult(RESULT_OK, cam);
            finish();
        }
        else {
            Toast.makeText(this, "No camera available.", Toast.LENGTH_SHORT);
            finish();
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
     * Creating a temporary file to store the photo in  RAM.
     * The image is stored in the default temporary directory until the user kills the app.
     * @return
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

    /**
     * This methods checks if any image has been captured and if the capture exited normally.
     * If those conditions are verified, the captured photo can be extracted.
     * The method checks if the request code matches with the new activity request code .
     * @param requestCode The code the activity has been launched for.
     * @param resultCode The given result when the activity is over.
     * @param data The intent which has just ended.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("RESULT", "onActivityResult:");
        if (requestCode == Constants.REQUEST_CAPTURE  && resultCode == Activity.RESULT_OK)
            this.setResult(RESULT_OK, cam);
        else{
            this.setResult(RESULT_CANCELED);
        }
    }

    /**
     * This method launches the camera and allows the user to capture a picture.
     * An Intent is launched to start the camera.
     * The captured image is placed in a temporary file then in a URI.
     * A URI is needed to store the captured image.
     * the putExtra method tells to the camera that the image needs to be stored in this URI.
     * The data captured by the camera are stored in the URI and the result code is sent.
     */
    public void launchCamera(){

        cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoCaptured = createTemporaryFile();
        photoURI = Uri.fromFile(photoCaptured);

        cam.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        this.startActivityForResult(cam, Constants.REQUEST_CAPTURE);


    }
}





