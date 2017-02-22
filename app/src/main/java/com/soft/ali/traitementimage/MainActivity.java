package com.soft.ali.traitementimage;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Img image;
    ImgView iv;
    Intent cam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImgView) findViewById(R.id.iv);
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
                startActivityForResult(cam, Constants.REQUEST_CAPTURE);
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
        if (requestCode == Constants.REQUEST_CAPTURE && resultCode == RESULT_OK && data != null) {
            image.clearMemory();
            ClipData cd = data.getClipData();
            ClipData.Item u = cd.getItemAt(0);
            Uri photo = u.getUri();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photo);
                image = new Img(bitmap);
                iv.setImageBitmap(image.getOriginalBitmap());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (data == null){
            Toast.makeText(getBaseContext(), "Capture Canceled", Toast.LENGTH_SHORT);
        }
    }
}
