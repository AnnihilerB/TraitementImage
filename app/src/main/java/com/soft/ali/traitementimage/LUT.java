package com.soft.ali.traitementimage;

import android.graphics.Color;

/**
 * Created by ali on 10/02/2017.
 */

public class LUT {

    private static final int CONTRAST = 255;

    private  int[] table;
    private int min = 255;
    private int max = 0;

    public LUT(){
        table = new int[Constants.NBCOLORS];
    }

    public int getValueAt(int index){
     return table[index];
    }

    public void generate(Img image){
        float[] hsv = new float[3];
        int[] pixels = image.getArraypixel();

        dynamique(pixels);

        for (int i = 0; i < table.length; i++){
            Color.colorToHSV(pixels[i], hsv);
            table[i] = getExtensionValue((int)hsv[Constants.HSV_SATURATION]);
        }
    }

    public int getExtensionValue(int value){
        return ( CONTRAST * (value - min) ) / (max - min);
    }

    private void dynamique(int[] pixels){

        float[] hsv = new float[3];

        for (int i = 0; i< pixels.length; i++){
            Color.colorToHSV(pixels[i], hsv);
            min = Math.min(min, (int)hsv[Constants.HSV_SATURATION]);
            max = Math.max(max, (int)hsv[Constants.HSV_SATURATION]);
        }
    }







}
