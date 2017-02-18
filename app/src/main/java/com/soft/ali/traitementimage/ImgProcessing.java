package com.soft.ali.traitementimage;

import android.graphics.Color;
import android.util.Log;

/**
 * Created by ali on 27/01/2017.
 */

public class ImgProcessing {

    private static Img image;

    public static void colorize() {
        float hsv[] = new float[3];
        int pixels[] = image.getArraypixel();
        for (int i = 0; i < pixels.length; ++i) {

            Color.colorToHSV(pixels[i], hsv);
            hsv[0] = (float) 120; // TODO: Work with the color picker.
            pixels[i] = Color.HSVToColor(hsv);
        }
    }

    public static void histogramEqualization(){

        int channel = Constants.HSV_VIBRANCE;

        Histogram hist = new Histogram();
        hist.generateHSVHistogram(image, channel);

        float nbPixels = hist.nbpixels;
        int pixels[] = image.getArraypixel();

        for (int i = 0; i < pixels.length; i++){
            float[] hsv = new float[3];
            Color.colorToHSV(pixels[i], hsv);
            int val = Math.round(hsv[channel]);
            hsv[channel] = (hist.getCumulativeHistogramValueAt(val)  * 255) / nbPixels;
            pixels[i] = Color.HSVToColor(hsv);
        }
    }


    public void extensionDynamique() {
        //La faire en HSV et ne pas toucher la teinte. Faire l'extension sur S puis repasser en RGB.
    }

    public void convolution() {
        /* Filtre de taille impaire toujours.
        Pose le filtre sur l'aimge, le filtre calcule la valeur du pixel central en mulitipliant la valeur des pixels par la valeur du masque 1 à1.
        premiere case pixel * premiere case masque etc..
         */
    }

    public static void setImage(Img imagebase){
        image = imagebase;
    }

    public static Img getImage(){
        return image;
    }

}




