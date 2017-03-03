package com.soft.ali.traitementimage;

import android.graphics.Color;

/**
 * Created by ali on 11/02/2017.
 */

public class Histogram {

    /**
     * Class handling an histogram and a cumulated histogram.
     */

    private int[] hist;
    private int[] cumulHist;
    private int nbpixels = 0;

    public Histogram() {
        hist = new int[Constants.NBCOLORS];
        cumulHist = new int[Constants.NBCOLORS];
    }

    /**
     * This method generates an histogram based on HSV value.
     * @param pix pixel array to be processed.
     * @param channel the HSV channel to be used.
     */
    public void generateHSVHistogram(int []pix, int channel) {

        nbpixels = pix.length;

        float[] hsv = new float[3];

        for (int i = 0; i < pix.length; i++) {
            Color.colorToHSV(pix[i], hsv);
            //Rescaling the value
            int val  = (int)(hsv[channel] * 255);
            hist[val]++;
        }

        int cumul = 0;
        for (int i = 0; i < Constants.NBCOLORS; i++){
            cumul = hist[i] + cumul;
            cumulHist[i] = cumul;
        }
    }

    /**
     * Getting the value in the cumulative histogram.
     * @param index index of the array
     * @return the value gathered at index.
     */
    public int getCumulativeHistogramValueAt(int index){
        return cumulHist[index];
    }

    /**
     * Getting the value in the histogram.
     * @param index index of the array
     * @return the value gathered at index.
     */
    public int getHistogramValueAt(int index){
        return hist[index];
    }

    public int getNbPixels(){
        return nbpixels;
    }


}


