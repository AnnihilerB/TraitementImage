package com.soft.ali.traitementimage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ali on 27/01/2017.
 */

/**
 * Making our own ImageView class allows us to control what's displayed on the screen.
 * We will keep the basic display (Android rescaling)  and then it will be possible to zoom (in and out) and scroll on the image.
 */

public class ImgView  extends ImageView {

    public ImgView(Context context) {
        super(context);
    }

    public ImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
