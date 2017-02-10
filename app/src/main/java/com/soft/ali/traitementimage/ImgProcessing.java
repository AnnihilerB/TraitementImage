package com.soft.ali.traitementimage;

import android.graphics.Color;

/**
 * Created by ali on 27/01/2017.
 */

public class ImgProcessing {

    Img image;

    public void colorize(){
        float hsv[] = new float[3];
        int pixels[] = image.getArraypixel();
        for (int i = 0; i < pixels.length; ++i) {

            Color.colorToHSV(pixels[i], hsv);
            hsv[0] = (float) 120; // TODO: Work with the color picker.
            pixels[i] = Color.HSVToColor(hsv);
        }


        }
    }




