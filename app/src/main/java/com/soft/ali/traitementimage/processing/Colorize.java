package com.soft.ali.traitementimage.processing;

import com.soft.ali.traitementimage.ImgProcessing;

/**
 * Created by ali on 17/04/2017.
 */

public class Colorize extends ImgProcessing implements InterProcessing {

    private int color;

    public Colorize(int color){
        this.color = color;
    }

    /**
     * Apply the colorize effect on an interval of the image.
     * @param lower lower limit of the image.
     * @param upper upper limit of the image.
     */
    @Override
    public void process(int lower, int upper) {
        ImgProcessing.colorize(this.color, lower, upper);
    }
}
