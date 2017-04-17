package com.soft.ali.traitementimage.processing;

import com.soft.ali.traitementimage.ImgProcessing;

/**
 * Created by ali on 17/04/2017.
 */

public class Sepia extends ImgProcessing implements InterProcessing {

    /**
     * Apply the sepia effect on a part of the image.
     * @param lower lower limit of the image.
     * @param upper upper limit of the image.
     */
    @Override
    public void process(int lower, int upper) {
        ImgProcessing.sepia(lower, upper);
    }
}
