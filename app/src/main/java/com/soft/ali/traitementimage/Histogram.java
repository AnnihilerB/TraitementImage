package com.soft.ali.traitementimage;

import android.graphics.Color;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by ali on 11/02/2017.
 */

public class Histogram {

    private int[] hist;
    private int[] cumulHist;
    private int nbpixels = 0;

    public Histogram() {
        hist = new int[Constants.NBCOLORS];
        cumulHist = new int[Constants.NBCOLORS];
    }

    public void generateHSVHistogram(Img image, int channel) {

        int[] pixels = image.getArraypixel();
        float[] hsv = new float[3];

        for (int i = 0; i < pixels.length; i++) {
            Color.colorToHSV(pixels[i], hsv);
            int val  = (int)(hsv[channel] * 255);
            hist[val]++;
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

    public int getNbPixels(){
        return nbpixels;
    }


}


