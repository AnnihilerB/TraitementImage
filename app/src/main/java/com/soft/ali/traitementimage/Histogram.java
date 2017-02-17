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
            hist[Math.round( hsv[Constants.HSV_VIBRANCE])]++;
        }

        int cumul = 0;
        for (int i = 0; i < Constants.NBCOLORS; i++){
            cumul = hist[i] + cumul;
            cumulHist[i] = cumul;
        }
        nbpixels = cumulHist[255];
    }

    public int getCumulativeHistogramValueAt(int index){
        return cumulHist[index];
    }

    public int getHistogramValueAt(int index){
        return hist[index];
    }


}


