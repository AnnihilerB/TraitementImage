package com.soft.ali.traitementimage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity{

    private Img image;
    private  ImgView imgView;
    private Uri photoURI;
    private File photoCaptured;
    private Context mainContext;
    private int hueValue=0;
    private int colorValue=0;
    private int sizeFilterValue=3;
    private int typeFilterValue=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mainContext = getApplicationContext();

        //Creating a blank image.
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.poivron);
        image = new Img(bitmap);
        ImgProcessing.setImage(image);
        imgView = (ImgView)findViewById(R.id.iv);
        imgView.setOnTouchListener(new ScrollZoomListener());

        //Button initialization.
        Button buttonGray = (Button)findViewById(R.id.buttonGray);
        Button buttonColorize= (Button)findViewById(R.id.buttonColorize);
        Button buttonEqualization = (Button)findViewById(R.id.buttonEqualization);
        Button buttonExtension = (Button)findViewById(R.id.buttonExtension);
        Button buttonConvolution = (Button)findViewById(R.id.buttonConvolution);
        Button buttonOverexposure = (Button)findViewById(R.id.buttonOverexposure);
        Button buttonFusion = (Button)findViewById(R.id.buttonFusion);
        Button buttonRes = (Button)findViewById(R.id.buttonReset);
        Button buttonValue = (Button)findViewById(R.id.buttonValue);
        Button buttonIsolate = (Button)findViewById(R.id.buttonIsolate);
        Button buttonSepia = (Button)findViewById(R.id.buttonSepia);

        FloatingActionsMenu menuActions = (FloatingActionsMenu)findViewById(R.id.actionsMenu);
        FloatingActionButton fabCamera = (FloatingActionButton)findViewById(R.id.fabCamera);
        FloatingActionButton fabGallerie = (FloatingActionButton)findViewById(R.id.fabGallerie);

        final FrameLayout blackBackground = (FrameLayout)findViewById(R.id.background);

        menuActions.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                blackBackground.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                blackBackground.setVisibility(View.INVISIBLE);
            }
        });

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
                if (Utils.deviceHasCamera(getPackageManager()))
                    launchCamera();
                else
                    Toast.makeText(mainContext, "No camera available.", Toast.LENGTH_SHORT).show();
            }
        });

        fabGallerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
                startGallery();
            }
        });




        buttonGray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgProcessing.toGray();
                Utils.updateImageView(image, imgView);
            }
        });

        buttonColorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgProcessing.colorize(hueValue);
                Utils.updateImageView(image, imgView);
            }
        });

        buttonEqualization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgProcessing.histogramEqualization();
                Utils.updateImageView(image, imgView);
            }
        });

        buttonExtension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgProcessing.extensionDynamique();
                Utils.updateImageView(image, imgView);
            }
        });

        buttonOverexposure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgProcessing.overexposure();
                Utils.updateImageView(image, imgView);
            }
        });

        buttonIsolate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgProcessing.isolate(colorValue);
                Utils.updateImageView(image, imgView);
            }
        });

        buttonSepia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgProcessing.sepia();
                Utils.updateImageView(image, imgView);
            }
        });

        buttonFusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmapText = BitmapFactory.decodeResource(getResources(), R.mipmap.text);
                ImgProcessing.fusion(bitmapText);
                Utils.updateImageView(image, imgView);
            }
        });

        buttonConvolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgProcessing.convolution(sizeFilterValue, typeFilterValue);
                Utils.updateImageView(image, imgView);
            }
        });

        buttonRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.resetArrayPixels();
                imgView.setImageBitmap(image.getOriginalBitmap());
            }
        });

        buttonValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ValuePicker.class);
                startActivityForResult(intent, Constants.PICKER_VALUE);
            }
        });
    }


    /**
     * This method will be used when an activity returns.
     * It allows us to extract the data we need and use them later.
     * @param requestCode is used to know which activity has returned.
     * @param resultCode  allows us to know if the actiity has ended well or not.
     * @param data  the activity returns.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //================== CAMERA ==================//

        if (requestCode == Constants.REQUEST_CAPTURE && resultCode == RESULT_OK) {
            //Cleaning the RAM.
            image.clearMemory();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                image = new Img(bitmap);
                ImgProcessing.setImage(image);
                imgView.setImageBitmap(image.getOriginalBitmap());
                //Deleting the photo when loaded in RAM.
                photoCaptured.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == Constants.REQUEST_CAPTURE  && resultCode == RESULT_CANCELED) {
            Toast.makeText(mainContext, "Capture canceled", Toast.LENGTH_SHORT).show();
        }

        //================== SAVING  ==================//

        if (requestCode == Constants.WRITE_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri FileToWrite = data.getData();
            Utils.writeImageToStorage( ((BitmapDrawable) imgView.getDrawable()).getBitmap(), FileToWrite, getContentResolver(), mainContext);
        }
        else if (requestCode == Constants.WRITE_IMAGE && resultCode == RESULT_CANCELED )
            Toast.makeText(mainContext, "Save canceled.", Toast.LENGTH_SHORT).show();

        //================== LOADING FROM GALLERY ==================//

        if (requestCode == Constants.LOAD_IMAGE && resultCode == RESULT_OK  && data != null){
            Uri uri = data.getData();
            try {
                image.clearMemory();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image = new Img(bitmap);
                ImgProcessing.setImage(image);
                imgView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(mainContext, "Error getting bitmap from storage.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else if (requestCode == Constants.LOAD_IMAGE && resultCode == RESULT_CANCELED)
            Toast.makeText(mainContext, "Loading canceled.", Toast.LENGTH_SHORT).show();


        //================== VALUE PICKER ===================//

        if (requestCode == Constants.PICKER_VALUE && resultCode == RESULT_OK && resultCode == RESULT_OK && data != null){
            hueValue = data.getIntExtra("hueValue",0);
        }

    }
    /**
     * This method launches the camera and allows the user to capture a picture.
     * An Intent is launched to start the camera.
     * The captured image is placed in a temporary file then in a URI.
     * A URI is needed to store the captured image.
     * the putExtra method tells to the camera that the image needs to be stored in this URI.
     * The data captured by the camera are stored in th URI and the result code is sent.
     */
    private void launchCamera() {
        Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoCaptured = Utils.createTemporaryFile(mainContext);
        photoURI = Uri.fromFile(photoCaptured);
        cam.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(cam, Constants.REQUEST_CAPTURE);
    }

    private void startGallery(){
        // Create the Intent for Image Gallery.
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, Constants.LOAD_IMAGE);
    }

    /**
     * This method checks and asks for permissions.
     * The method is used only if the API is >= 23.
     * If permission has not been granted yet, the app asks the user to use the feature.
     */
    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.LOAD_PERMISSIONS);

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.CAMERA_PERMISSIONS);

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_PERMISSIONS);
        }
    }

}



