package com.soft.ali.traitementimage;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity{

    Img image;
    ImgView iv;
    Uri photoURI;
    File photoCaptured;
    Context mainContext;
    int chosenColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = new Img();
        iv = (ImgView)findViewById(R.id.iv);

        LinearGradient test = new LinearGradient(0.f, 0.f, 600.f, 0.0f, new int[]{0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF, 0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF}, null, Shader.TileMode.CLAMP);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(test);

        final SeekBar seekBarFont = (SeekBar) findViewById(R.id.seekbar_font);
        seekBarFont.setProgressDrawable(shape);
        seekBarFont.setMax(256 * 7 - 1);

        Button button = (Button)findViewById(R.id.cam);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGallery();
            }
        });

        seekBarFont.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int r = 0;
                    int g = 0;
                    int b = 0;

                    if (progress < 256) {
                        b = progress;
                    } else if (progress < 256 * 2) {
                        g = progress % 256;
                        b = 256 - progress % 256;
                    } else if (progress < 256 * 3) {
                        g = 255;
                        b = progress % 256;
                    } else if (progress < 256 * 4) {
                        r = progress % 256;
                        g = 256 - progress % 256;
                        b = 256 - progress % 256;
                    } else if (progress < 256 * 5) {
                        r = 255;
                        g = 0;
                        b = progress % 256;
                    } else if (progress < 256 * 6) {
                        r = 255;
                        g = progress % 256;
                        b = 256 - progress % 256;
                    } else if (progress < 256 * 7) {
                        r = 255;
                        g = 255;
                        b = progress % 256;
                    }
                    seekBarFont.setBackgroundColor(Color.argb(255, r, g, b));
                    chosenColor = Color.argb(255, r, g, b);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    /**
     * This method will be used when an activity returns.
     * It allows us to extract the data we need and use them later.
     *
     * @param requestCode is used to know which activity has returned.
     * @param resultCode  allows us to know if the actiity has ended well or not.
     * @param data        the activity returns.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Capturing a photo via the camera doesn't return any data.
        if (requestCode == Constants.REQUEST_CAPTURE && resultCode == RESULT_OK) {
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
        if (requestCode == Constants.REQUEST_CAPTURE && resultCode == RESULT_CANCELED) {
            Toast.makeText(mainContext, "Capture canceled", Toast.LENGTH_SHORT);
        }
        if (requestCode == Constants.WRITE_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri FileToWrite = data.getData();
            getPathFromURI(FileToWrite);
        }
        if (requestCode == Constants.LOAD_IMAGE && resultCode == RESULT_OK ){
            Uri uri = data.getData();
            try {
                image.clearMemory();
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Explain to the user why we need to read the contacts
                    }

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            42);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant that should be quite unique

                    return;
                }
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image = new Img(bitmap);
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(mainContext, "Error getting bitmap form storage.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }


    }

    /**
     * This method checks if the device has any camera (front or back).
     * The verification is made by accessing the package manager which gives information about the various application packages.
     * As the phone camera is recognized as an app, checking if a camera is available is possible via the package manager.
     *
     * @return true if the device has a camera, false instead.
     */
    private boolean deviceHasCamera() {
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
    private void launchCamera() {
        Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoCaptured = createTemporaryFile();
        photoURI = Uri.fromFile(photoCaptured);

        cam.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(cam, Constants.REQUEST_CAPTURE);
    }

    /**
     * Create a temporary file in memory to store the photo.
     * The file is deleted when the user confirms the capture.
     *
     * @return The path of the temporary file.
     */
    private File createTemporaryFile() {
        File temp = Environment.getExternalStorageDirectory();
        temp = new File(temp.getAbsolutePath() + "/.temp");
        if (!temp.exists()) {
            temp.mkdir();
        }
        try {
            return File.createTempFile(Constants.FILE_NAME, Constants.EXTENSION, temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startGallery (){
        // Create the Intent for Image Gallery.
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, Constants.LOAD_IMAGE);
    }

    private void getPathFromURI(Uri imageURI) {
        ContentResolver cr = getContentResolver();
        ParcelFileDescriptor fd;
        FileOutputStream fos;
        try {
            fd = cr.openFileDescriptor(imageURI, "rw");
            fos = new FileOutputStream(fd.getFileDescriptor());
            Bitmap bmp = ((BitmapDrawable) iv.getDrawable()).getBitmap();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



