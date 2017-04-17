package com.soft.ali.traitementimage.processing;

import com.soft.ali.traitementimage.ImgProcessing;

/**
 * Created by ali on 17/04/2017.
 */

public class Isolate extends ImgProcessing implements InterProcessing {

    private int color;

    public Isolate(int color){
        this.color = color;
    }

    /**
     * Isolate the selected color on a part of the image.
     * @param lower lower limit of the image.
     * @param upper upper limit of the image.
     */
    @Override
    public void process(int lower, int upper) {
        ImgProcessing.isolate(this.color, lower, upper);
    }
}
