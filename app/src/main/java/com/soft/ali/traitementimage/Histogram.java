package com.soft.ali.traitementimage;

import android.graphics.Color;

/**
 * Created by ali on 11/02/2017.
 */

public class Histogram {

    int[] hist;
    int [] cumulHist;

    public Histogram() {
        hist = new int[Constants.NBCOLORS];
        cumulHist = new int[Constants.NBCOLORS];
    }

    public void generateHisotgram(Img image) {

        float[] hsv = new float[3];
        int[] pixels = image.getArraypixel();

        for (int i = 0; i < pixels.length; i++) {

            Color.colorToHSV(pixels[i], hsv);
            hist[(int) hsv[2]]++;
        }

        int cumul = 0;
        for (int i = 0; i < Constants.NBCOLORS; i++){
            cumul = cumulHist[i] + cumul;
            cumulHist[i] = cumul;
        }
    }
}


