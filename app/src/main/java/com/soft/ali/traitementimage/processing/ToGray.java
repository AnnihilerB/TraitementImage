package com.soft.ali.traitementimage.processing;

import com.soft.ali.traitementimage.ImgProcessing;

/**
 * Created by ali on 29/03/2017.
 */

public class ToGray extends ImgProcessing implements InterProcessing {

    /**
     * Transform in gray level a part of the image.
     * @param lower lower limit of the image.
     * @param upper upper limit of the image.
     */
    @Override
    public void process(int lower, int upper) {
        toGray(lower, upper);
    }
}
