package com.soft.ali.traitementimage.processing;

import com.soft.ali.traitementimage.ImgProcessing;

/**
 * Created by ali on 17/04/2017.
 */



public class ContrastAdjust extends ImgProcessing implements InterProcessing {

    private int[] originalPixels;
    int contrast;

    public ContrastAdjust(int[] pixels, int contrast){
        this.originalPixels = pixels;
        this.contrast = contrast;
    }



    @Override
    public void process(int lower, int upper) {
        ImgProcessing.contrastAdjust(this.originalPixels, this.contrast, lower, upper);

    }
}
