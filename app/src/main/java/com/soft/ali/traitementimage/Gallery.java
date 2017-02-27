package com.soft.ali.traitementimage;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

/**
 * Created by ali on 27/01/2017.
 */

public class Gallery extends AppCompatActivity {

    // Image loading result to pass to startActivityForResult method.
    private static int LOAD_IMAGE_RESULTS = 1;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startGallery();
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("byteArray", bs.toByteArray());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            // Now we need to set the GUI ImageView data with data read from the picked file.
            bitmap = BitmapFactory.decodeFile(imagePath);

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

        }
    }

    public void startGallery (){
        // Create the Intent for Image Gallery.
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, LOAD_IMAGE_RESULTS);
    }
}