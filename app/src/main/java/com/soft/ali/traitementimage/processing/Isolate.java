package com.soft.ali.traitementimage.processing;

import com.soft.ali.traitementimage.ImgProcessing;

/**
 * Created by ali on 17/04/2017.
 */

public class Isolate extends ImgProcessing implements InterProcessing {

    int color;

    public Isolate(int color){
        this.color = color;
    }

    @Override
    public void process(int lower, int upper) {
        ImgProcessing.isolate(this.color, lower, upper);
    }
}
