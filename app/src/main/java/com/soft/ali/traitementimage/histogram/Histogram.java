package com.soft.ali.traitementimage.histogram;

import android.graphics.Color;

import com.soft.ali.traitementimage.Constants;

/**
 * Created by ali on 11/02/2017.
 */

public class Histogram {

    /**
     * Class handling an histogram and a cumulated histogram.
     */

    private int[] hist;

    public Histogram() {
        hist = new int[Constants.NBCOLORS];
    }

    /**
     * This method generates an histogram based on HSV value.
     * @param pix pixel array to be processed.
     * @param channel the HSV channel to be used.
     */
    public void generateHSVHistogram(int []pix, int channel) {

        float[] hsv = new float[3];

        for (int i = 0; i < pix.length; i++) {
            Color.colorToHSV(pix[i], hsv);
            //Rescaling the value
            int val  = (int)(hsv[channel] * 255);
            hist[val]++;
        }
    }

    /**
     * Getting the value in the histogram.
     * @param index index of the array
     * @return the value gathered at index.
     */
    public int getHistogramValueAt(int index){
        return hist[index];
    }

    /**
     * Return the histogram of the current image.
     * @return the histogram.
     */
    public int[] getHistogram(){
        return this.hist;
    }


}


