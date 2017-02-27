package com.soft.ali.traitementimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonGallery;
    private Button buttonProcessing;
    private ImageView image;
    private Img imagea;
    private ImgProcessing imgProcessing;
    private Bitmap bitmap ;
    private int chosenColor;
    private Bitmap bitmaptext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonGallery = (Button)findViewById(R.id.button);
        buttonProcessing = (Button)findViewById(R.id.button2);
        image = (ImageView)findViewById(R.id.imageView);
        buttonGallery.setOnClickListener(this);
        buttonProcessing.setOnClickListener(this);
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        imagea = new Img(bitmap);
        imgProcessing = new ImgProcessing(imagea);
        bitmaptext = BitmapFactory.decodeResource(getResources(), R.mipmap.texte);

        LinearGradient test = new LinearGradient(0.f, 0.f, 600.f, 0.0f, new int[] { 0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF, 0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF}, null, Shader.TileMode.CLAMP);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(test);

        final SeekBar seekBarFont = (SeekBar)findViewById(R.id.seekbar_font);
        seekBarFont.setProgressDrawable(shape);
        seekBarFont.setMax(256*7-1);

        seekBarFont.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    int r = 0;
                    int g = 0;
                    int b = 0;

                    if(progress < 256){
                        b = progress;
                    } else if(progress < 256*2) {
                        g = progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*3) {
                        g = 255;
                        b = progress%256;
                    } else if(progress < 256*4) {
                        r = progress%256;
                        g = 256 - progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*5) {
                        r = 255;
                        g = 0;
                        b = progress%256;
                    } else if(progress < 256*6) {
                        r = 255;
                        g = progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*7) {
                        r = 255;
                        g = 255;
                        b = progress%256;
                    }
                    seekBarFont.setBackgroundColor(Color.argb(255, r, g, b));
                    chosenColor=Color.argb(255, r, g, b);
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

    public void onClick(View v) {

        if (v.getId() == R.id.button) {
            Intent intent = new Intent(MainActivity.this, Gallery.class);
            startActivityForResult(intent, 1);
        }

        if(v.getId() == R.id.button2) {
            imgProcessing.Colorize(chosenColor);
            imgProcessing.fusion(bitmaptext);
            imgProcessing.Overexposure();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
                    image.setImageBitmap(bitmap);
                }
                break;
            }
        }
    }

}
