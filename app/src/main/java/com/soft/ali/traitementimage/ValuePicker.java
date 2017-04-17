package com.soft.ali.traitementimage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * ValuePicker is an activity used to help the user for choose different values to applied for the image processing.
 * The hue value for "colorize", a color value for "isolate", and the size and the type of a filter for the convolution.
 * Each value is choose by a seek bar progress, when the user has finished to choose the value. P
 * Press the "ok" button permit to create an intent that is going to store each values and send the to the main activity, then the value activity stop.
 */

public class ValuePicker extends AppCompatActivity{

    private int hueValue=120;
    private int colorValue=Color.RED;
    private int sizeFilterValue=3;
    private int typeFilterValue=1;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value);

        Button buttonOK = (Button)findViewById(R.id.buttonOK);

        final SeekBar seekBarHue = (SeekBar) findViewById(R.id.seekBarHue);
        seekBarHue.setMax(360);
        //0xFFFF0000,0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF, 0xFF0000FF, 0xFFFF00FF,0xFFFF0000 //values for background
        final TextView hueValueText = (TextView) findViewById(R.id.textHueValue);

        final SeekBar seekBarColor = (SeekBar) findViewById(R.id.seekBarColor);
        //Displaying a gradient so the user can see which color he is picking.
        LinearGradient linearGradient = new LinearGradient(0.f, 0.f, 720.f, 0.0f, new int[]{0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF, 0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF}, null, Shader.TileMode.CLAMP); //linear gradient : Create a shader that draws a linear gradient along a line. Draw a color bar from black to white.
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(linearGradient);
        seekBarColor.setProgressDrawable(shape);
        seekBarColor.setMax(256*7-1);

        final SeekBar seekBarSizeFilter = (SeekBar) findViewById(R.id.seekBarSizeFilter);
        final TextView sizeFilterValueText = (TextView) findViewById(R.id.textSizeFilterValue);
        seekBarSizeFilter.incrementProgressBy(2);

        final SeekBar seekBarTypeFilter = (SeekBar) findViewById(R.id.seekBarTypeFilter);
        final TextView typeFilterValueText = (TextView) findViewById(R.id.textTypeFilterValue);

        /**
         * When the user touch the seek bar, it change the progress value and launch the listener corresponding to the seekbar.
         * The textView change and the global values hueValue, colorValue, sizeFilterValue, typeFilterValue.
         */
        seekBarHue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hueValueText.setText(String.valueOf(seekBar.getProgress()));
                hueValue = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { //In function of the progress value, a rgb pixel value is calculated and store in the colorValue parameter.
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

                    seekBarColor.setBackgroundColor(Color.argb(255, r, g, b));
                    colorValue = Color.argb(255, r, g, b);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarSizeFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = 2*progress+1;
                sizeFilterValue = value;
                sizeFilterValueText.setText(String.valueOf(value));
                Log.i("CONV", "Dans seekbar : " + String.valueOf(value));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarTypeFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                typeFilterValue = seekBarTypeFilter.getProgress();
                switch (typeFilterValue){
                    case 0: typeFilterValueText.setText("Average");break;
                    case 1: typeFilterValueText.setText("Gauss");break;
                    case 2: typeFilterValueText.setText("Sobel");break;
                    case 3: typeFilterValueText.setText("Laplace");break;
                    case 4: typeFilterValueText.setText("Laplace2");break;
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //Sending values selected by the user.
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra("hueValue", hueValue);
                result.putExtra("colorValue", colorValue);
                result.putExtra("sizeFilterValue", sizeFilterValue);
                result.putExtra("typeFilterValue", typeFilterValue);
                setResult(RESULT_OK, result);
                finish();
            }
        });
    }
}
