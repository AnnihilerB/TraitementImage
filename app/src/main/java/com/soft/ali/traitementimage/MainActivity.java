package com.soft.ali.traitementimage;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int PHOTO_CAPTURED = 2;
    Img image;
    ImageView iv;
    Uri photo;
    Intent cam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);
        iv.setOnTouchListener(new ScrollZoomListener());

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contraste);
        image = new Img(bitmap);

        Button b = (Button) findViewById(R.id.ega);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgProcessing.setImage(image);
                //ImgProcessing.histogramEqualization();
                //Bitmap res = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
                //res.setPixels(image.getArraypixel(), 0, image.getWidth(), 0, 0 , image.getWidth(), image.getHeight());
                //iv.setImageBitmap(res);
                cam = new Intent(getBaseContext(), CameraActivity.class);
                startActivityForResult(cam, PHOTO_CAPTURED);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_CAPTURED && resultCode == RESULT_OK && data != null) {
            Log.i("CAM", "Activity");
            Toast.makeText(getApplicationContext(), "CameraActivity has returned.", Toast.LENGTH_SHORT);
            ClipData cd = data.getClipData();
            ClipData.Item u = cd.getItemAt(0);
            Uri photo = u.getUri();
            iv.setImageURI(photo);
            Log.i("CAM", "Activity");
        }
    }
}
