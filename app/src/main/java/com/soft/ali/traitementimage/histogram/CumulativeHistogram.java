package com.soft.ali.traitementimage.histogram;

import com.soft.ali.traitementimage.Constants;

/**
 * Created by ali on 28/03/2017.
 */

public class CumulativeHistogram {

    private int cumulHist[];

    public CumulativeHistogram(){
        cumulHist = new int[Constants.NBCOLORS];
    }

    /**
     * Generate the cumulative histogram of the image.
     * @param hist the histogram the cumulative Histogram is based on.
     */
    public void generateCumulativeHistogram(int hist[]){
        int sum = 0;
        for (int i = 0; i < Constants.NBCOLORS; i++){
            sum = hist[i] + sum;
            cumulHist[i] = sum;
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
}
