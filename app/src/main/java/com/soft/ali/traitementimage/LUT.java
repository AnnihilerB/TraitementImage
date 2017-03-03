package com.soft.ali.traitementimage;

import android.graphics.Color;
import android.util.Log;

/**
 * Created by ali on 10/02/2017.
 */

public class LUT {

    /**
     * Class handling a Look Up table.
     * This table allows us to store results of a calculus.
     * The calculus is made 255 times maximum.
     */

    private  float[] table;
    private float min;
    private float max;
    public LUT(){
        table = new float[Constants.NBCOLORS];
    }

    public float getValueAt(int index){
        return table[index];
    }

    /**
     * Generate a LUT based on a HSV image.
     * The table is filled with value between 0 and 1.
     * @param image the image to be processed.
     */
    public void generateHSV(Img image){
        int[] pixels = image.getArraypixel();

        dynamicHSV(pixels);

        for (int i = 0; i < table.length; i++){
            table[i]= getExtensionValue(i);
        }
    }

    private float getExtensionValue(int value){
        return ((value - min) ) / (max - min);
    }

    /**
     * Method calculating th dynamic for an HSV image.
     * min and max are the average of the min and max of each RGB component.
     * @param pixels pixel array to be processed.
     */
    private void dynamicHSV(int[] pixels){

        float minR= 255;
        float minG= 255;
        float minB= 255;
        float maxR = 0;
        float maxG = 0;
        float maxB = 0;

        for (int i = 0; i< pixels.length; i++){
            minR = Math.min(minR, Color.red(pixels[i]));
            minG = Math.min(minG, Color.green(pixels[i]));
            minB = Math.min(minB, Color.blue(pixels[i]));

            maxR = Math.max(maxR, Color.red(pixels[i]));
            maxG = Math.max(maxG, Color.green(pixels[i]));
            maxB = Math.max(maxB, Color.blue(pixels[i]));
        }
        min =((minR+minG+minB) / 3)/(float)255;
        max = ((maxR+maxG+maxB) / 3)/(float)255;
    }
}
