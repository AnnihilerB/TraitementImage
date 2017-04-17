package com.soft.ali.traitementimage.processing;

import com.soft.ali.traitementimage.ImgProcessing;

/**
 * Created by ali on 17/04/2017.
 */

public class ExtendDynamism extends ImgProcessing implements InterProcessing {
    @Override
    public void process(int lower, int upper) {
        ImgProcessing.extendDynamism(lower, upper);
    }
}
