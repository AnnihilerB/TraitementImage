package com.soft.ali.traitementimage.processing;

import com.soft.ali.traitementimage.ImgProcessing;

/**
 * Created by ali on 29/03/2017.
 */

public class HistogramEqua extends ImgProcessing implements InterProcessing {

    /**
     * Extends the contrast on a part of the image via histogram equalization.
     * @param lower lower limit of the image.
     * @param upper upper limit of the image.
     */
    @Override
    public void process(int lower, int upper) {
        histogramEqualization(lower, upper);
    }
}
