package com.soft.ali.traitementimage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Valford on 01/03/2017.
 */

public class ValuePicker extends AppCompatActivity{

    private int hueValue=0;
    private int colorValue=0;
    private int sizeFilterValue=3;
    private int typeFilterValue=1;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value);

        Button buttonOK = (Button)findViewById(R.id.buttonOK);

        final SeekBar seekBarHue = (SeekBar) findViewById(R.id.seekBarHue);
        seekBarHue.setMax(360);
        //0xFFFF0000,0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF, 0xFF0000FF, 0xFFFF00FF,0xFFFF0000 //value for background
        final TextView hueValueText = (TextView) findViewById(R.id.textHueValue);

        final SeekBar seekBarColor = (SeekBar) findViewById(R.id.seekBarColor);
        seekBarColor.setMax(300);
        LinearGradient linearGradient = new LinearGradient(0.f, 0.f, 600.f, 0.0f, new int[]{0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF, 0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF}, null, Shader.TileMode.CLAMP);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(linearGradient);
        seekBarColor.setProgressDrawable(shape);

        final SeekBar seekBarSizeFilter = (SeekBar) findViewById(R.id.seekBarSizeFilter);
        final TextView sizeFilterValueText = (TextView) findViewById(R.id.textSizeFilterValue);

        final SeekBar seekBarTypeFilter = (SeekBar) findViewById(R.id.seekBarTypeFilter);
        final TextView typeFilterValueText = (TextView) findViewById(R.id.textTypeFilterValue);

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
                sizeFilterValueText.setText(String.valueOf(seekBar.getProgress()));
                sizeFilterValue = progress;
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
                typeFilterValueText.setText(String.valueOf(seekBar.getProgress()));
                typeFilterValue = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra("hueValue", hueValue);
                result.putExtra("colorValue", colorValue);
                result.putExtra("sizeFilterValue", sizeFilterValue);
                result.putExtra("typeFilterValue", typeFilterValue);

                System.out.println("!!!!!!!!!!!!!!" + hueValue);
                System.out.println("!!!!!!!!!!!!!!" + colorValue);
                System.out.println("!!!!!!!!!!!!!!" + sizeFilterValue);
                System.out.println("!!!!!!!!!!!!!!" + typeFilterValue);

                setResult(RESULT_OK, result);
                finish();
            }
        });
    }
}
