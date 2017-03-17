package com.soft.ali.traitementimage;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ali on 28/02/2017.
 */

public class Utils {


    /**
     * This method checks if the device has any camera (front or back).
     * The verification is made by accessing the package manager which gives information about the various application packages.
     * As the phone camera is recognized as an app, checking if a camera is available is possible via the package manager.
     *
     * @return true if the device has a camera, false instead.
     */
    public static boolean deviceHasCamera(PackageManager packageManager) {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public static void writeImageToStorage(Bitmap bitmap, Uri imageURI, ContentResolver cr, Context context) {
        ParcelFileDescriptor fd;
        FileOutputStream fos;
        try {
            fd = cr.openFileDescriptor(imageURI, "rw");
            fos = new FileOutputStream(fd.getFileDescriptor());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error : No saving file found.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(context, "Error writing the file.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Create a temporary file in memory to store the photo.
     * The file is deleted when the user confirms the capture.
     *
     * @return The path of the temporary file.
     */
    public static File createTemporaryFile(Context context) {
        File temp = Environment.getExternalStorageDirectory();
        temp = new File(temp.getAbsolutePath() + "/.temp");
        if (!temp.exists()) {
            temp.mkdir();
        }
        try {
            return File.createTempFile(Constants.FILE_NAME, Constants.EXTENSION, temp);
        } catch (IOException e) {
            Toast.makeText(context, "Error creating the temporary file.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }
    public static void updateImageView(Img image, ImgView imgView){
        Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.setPixels(image.getArrayPixel(), 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        imgView.setImageBitmap(bitmap);

    }
}
