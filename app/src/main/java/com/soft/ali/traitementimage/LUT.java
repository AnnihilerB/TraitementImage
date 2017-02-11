package com.soft.ali.traitementimage;

import android.graphics.Color;

/**
 * Created by ali on 10/02/2017.
 */

public class LUT {

    private static final int NBCOLORS = 256;
    private static final int CONTRAST = 255;

    private  int[] table;
    private int min = 255;
    private int max = 0;

    public LUT(){

        table = new int[NBCOLORS];

    }

    public int getValueAt(int index){
     return table[index];
    }

    public void generate(Img image){

        int[] pixels = image.getArraypixel();

        dynamique(pixels);

        float[] hsv = new float[3];

        for (int i = 0; i < table.length; i++){
            Color.colorToHSV(pixels[i], hsv);
            table[i] = getHistorgramEqualizationValue((int)hsv[1]);
        }
    }

    public int getHistorgramEqualizationValue(int value){
        return ( CONTRAST * (value - min) ) / (max - min);
    }

    private void dynamique(int[] pixels){

        float[] hsv = new float[3];

        for (int i = 0; i< pixels.length; i++){
            Color.colorToHSV(pixels[i], hsv);
            min = Math.min(min, (int)hsv[1]);
            max = Math.max(max, (int)hsv[1]);
        }
    }





}
