package com.soft.ali.traitementimage;

import android.graphics.Bitmap;

/**
 * A class used to get, from a bitmap, the array of pixels of a picture and its width and height.
 */

public class Img {

    private Bitmap originalImage;
    private int arrayPixel[];
    private int width;
    private int height;

    public  Img(){
        originalImage = null;
    }

    public Img (Bitmap bitmap) {
        originalImage = bitmap;
        width = originalImage.getWidth();
        height = originalImage.getHeight();
        arrayPixel = new int[width*height];
        bitmap.getPixels(arrayPixel, 0, width, 0,0, width, height);
    }

    /**
     * Returns a reference to the pixel array.
     * @return the pixel array.
     */
    public int [] getArrayPixel() {
        return arrayPixel;
    }

    /**
     * returns the width of an image.
     * @return width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * returns the height of an image.
     * @return height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Return the original bitmap.
     * Useful for restoring the image to its initial state.
     * @return the original bitmap
     */
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
            arrayPixel = null;
        }
    }

    /**
     * Method restoring the displayed image to its first state when it was loaded.
     */
    public void resetArrayPixels(){
        this.originalImage.getPixels(arrayPixel,0,width,0,0,width,height);
    }
}
