package com.soft.ali.traitementimage;

import android.graphics.Color;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by ali on 11/02/2017.
 */

public class Histogram {

    int[] hist;
    int[] cumulHist;
    float nbpixels = 0;

    public Histogram() {
        hist = new int[Constants.NBCOLORS];
        cumulHist = new int[Constants.NBCOLORS];
    }

    public void generateHisotgram(Img image) {

        float[] hsv = new float[3];
        int[] pixels = image.getArraypixel();

        for (int i = 0; i < pixels.length; i++) {

            Color.colorToHSV(pixels[i], hsv);
            hist[Math.round( hsv[Constants.HSV_SATURATION])]++;
        }

        int cumul = 0;
        for (int i = 0; i < Constants.NBCOLORS; i++){
            cumul = cumulHist[i] + cumul;
            cumulHist[i] = cumul;
        }
        nbpixels = cumulHist[255];

    }

    public float getCumulativeHistogramValueAt(int index){
        return cumulHist[index];
    }

    public float getHistogramValueAt(int index){
        return hist[index];
    }


}


