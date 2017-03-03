package com.soft.ali.traitementimage;

/**
 * Created by ali on 27/01/2017.
 */

import android.graphics.Bitmap;

/**
 * A class used to get, from a bitmap, the array of pixels of a picture and its width and height.
 */

public class Img {

    private Bitmap originalImage;
    private int arraypixel [];
    private int width;
    private int height;

    public  Img(){
        originalImage = null;
    }

    public Img (Bitmap bitmap) {

        originalImage = bitmap;
        width = originalImage.getWidth();
        height = originalImage.getHeight();
        arraypixel = new int[width*height];
        bitmap.getPixels(arraypixel, 0, width, 0,0, width, height);
    }

    public int [] getArraypixel() {
        return arraypixel;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bitmap getOriginalBitmap(){
        return originalImage;
    }

    /**
     * This method is used to clear the RAM when a second image is loaded.
     * As we use only one image at once, when the user selects a second image, the first one is
     * unallocated.
     * RAM is freed by dereferencing the array of pixels and by recycling the bitmap.
     */
    public void clearMemory(){
        if (originalImage != null) {
            originalImage.recycle();
            arraypixel = null;
        }
    }

    public void resetArrayPixels(){
        this.originalImage.getPixels(arraypixel,0,width,0,0,width,height);
    }
}
