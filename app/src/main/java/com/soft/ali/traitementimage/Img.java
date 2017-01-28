package com.soft.ali.traitementimage;

/**
 * Created by ali on 27/01/2017.
 */

import android.graphics.Bitmap;

/**
 * A class used to get, from a bitmap, the array of pixels of a picture and its width and height.
 */

public class Img {

    private int arraypixel [];
    private double width;
    private double height;

   public Img (Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        arraypixel = new int[width*height];
        bitmap.getPixels(arraypixel, 0, width, 0,0, width, height);
   }

    public int [] getArraypixel() {
        return arraypixel.clone();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
