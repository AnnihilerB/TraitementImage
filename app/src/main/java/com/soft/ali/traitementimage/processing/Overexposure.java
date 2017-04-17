package com.soft.ali.traitementimage.processing;

import com.soft.ali.traitementimage.ImgProcessing;

/**
 * Created by ali on 17/04/2017.
 */

public class Overexposure extends ImgProcessing implements InterProcessing {

    /**
     * Apply an effect of overexposure on a part of the image.
     * @param lower lower limit of the image.
     * @param upper upper limit of the image.
     */
    @Override
    public void process(int lower, int upper) {
        ImgProcessing.overexposure(lower, upper);
    }
}
