package com.soft.ali.traitementimage.processing;

import com.soft.ali.traitementimage.ImgProcessing;

/**
 * Created by ali on 17/04/2017.
 */

public class Colorize extends ImgProcessing implements InterProcessing {

    int color;

    public Colorize(int color){
        this.color = color;
    }

    @Override
    public void process(int lower, int upper) {
        ImgProcessing.colorize(this.color, lower, upper);
    }
}
