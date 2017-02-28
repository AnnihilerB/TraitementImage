package com.soft.ali.traitementimage;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by ali on 27/01/2017.
 */

public class ImgProcessing {

    private static final int AVERAGE = 0;
    private static final int GAUSS = 1;
    private static final int SOBEL = 2;
    private static final int LAPLACE = 3;
    private static final int LAPLACE2 = 4;

    private static Img image;

    public ImgProcessing(Img imagea) {
        image = imagea;
    }

   /* public static void colorize() {
        float hsv[] = new float[3];
        int pixels[] = image.getArraypixel();
        for (int i = 0; i < pixels.length; ++i) {

            Color.colorToHSV(pixels[i], hsv);
            hsv[0] = (float) 120; // TODO: Work with the color picker.
            pixels[i] = Color.HSVToColor(hsv);
        }
    }

    public static void histogramEqualization(){

        Histogram hist = new Histogram();
        hist.generateHisotgram(image);

        float nbPixels = hist.nbpixels;
        int pixels[] = image.getArraypixel();
        float[] hsv = new float[3];

        for (int i = 0; i < pixels.length; i++){
            Color.colorToHSV(pixels[i], hsv);
            int val = Math.round(hsv[Constants.HSV_VIBRANCE]);
            hsv[Constants.HSV_VIBRANCE] = hist.getCumulativeHistogramValueAt(val) * 255 / nbPixels;
            pixels[i] = Color.HSVToColor(hsv);
        }
    }


    public void extensionDynamique() {
        //La faire en HSV et ne pas toucher la teinte. Faire l'extension sur S puis repasser en RGB.
    }
    */

    public void convolution(int n, int typeFilter) {
        /* Filtre de taille impaire toujours.
        Pose le filtre sur l'aimge, le filtre calcule la valeur du pixel central en mulitipliant la valeur des pixels par la valeur du masque 1 à1.
        premiere case pixel * premiere case masque etc..
         */

        Filter filter = new Filter(n);

        if(typeFilter == AVERAGE){
            filter.setAverage();
            calculConvolution(filter.getFilter(), filter.getsizefilter());
        }

        if(typeFilter == GAUSS){
            double sigma = 0.8;
            filter.setGauss(sigma);
            calculConvolution(filter.getFilter(), filter.getsizefilter());
        }

        if(typeFilter == SOBEL){
            filter.setSobelHorizontal();
            calculConvolution(filter.getFilter(), filter.getsizefilter());
            filter.setSobelVertical();
            calculConvolution(filter.getFilter(), filter.getsizefilter());
        }

        if(typeFilter == LAPLACE){
            filter.setLaplace();
            calculConvolution(filter.getFilter(), filter.getsizefilter());
        }

        if(typeFilter == LAPLACE2){
            filter.setLaplace2();
            calculConvolution(filter.getFilter(), filter.getsizefilter());
        }
    }

    private void calculConvolution(int [][] filtermatrix, int sizefilter) {

        int pixels[] = image.getArraypixel();
        int originalpixels[]=pixels.clone();

        if(sizefilter == 3) {
            for (int i = 1; i < pixels.length; i++) {
                    pixels[i]=(originalpixels[i-1-image.getWidth()]*filtermatrix[0][0])
                            +(originalpixels[i-image.getWidth()]*filtermatrix[0][1])
                            +(originalpixels[i+1-image.getWidth()]*filtermatrix[0][2])
                            +(originalpixels[i-1]*filtermatrix[1][0])
                            +(originalpixels[i]*filtermatrix[1][1])
                            +(originalpixels[i+1]*filtermatrix[1][2])
                            +(originalpixels[i-1+image.getWidth()]*filtermatrix[2][0])
                            +(originalpixels[i+image.getWidth()]*filtermatrix[2][1])
                            +(originalpixels[i+1+image.getWidth()]*filtermatrix[2][2]);
            }
        }
    }

    public void Colorize(int chosenColor) {

        float hsv[] = new float[3];
        float hsvchosencolor[] = new float[3];
        Color.colorToHSV(chosenColor, hsvchosencolor);
        int pixels[] = image.getArraypixel();

        for(int i=0; i<pixels.length; i++){
            Color.colorToHSV(pixels[i], hsv);
            hsv[0] = hsvchosencolor[0];
            pixels[i] = Color.HSVToColor(hsv);
        }
    }

    public void Overexposure () {

        float hsv[] = new float[3];
        int pixels[] = image.getArraypixel();
        for(int i=0; i<pixels.length; i++){
            Color.colorToHSV(pixels[i], hsv);
            hsv[2] = (float) (hsv[2] + 0.20);
            pixels[i] = Color.HSVToColor(hsv);
        }
    }

   public void fusion (Bitmap bitmapText) {

       int widthtext = bitmapText.getWidth();
       int heighttext = bitmapText.getHeight();

       int moyred, moygreen, moyblue;
       int pixels[] = image.getArraypixel();
       int pixelarraytext[] = new int[widthtext * heighttext];
       if (pixelarraytext.length < pixels.length) {
           for (int i = 0; i < pixelarraytext.length; i++) {
               if (pixelarraytext[i] != Color.WHITE) {
                   moyred = (Color.red(pixels[i]) + Color.red(pixelarraytext[i])) / 2;
                   moygreen = (Color.green(pixels[i]) + Color.green(pixelarraytext[i])) / 2;
                   moyblue = (Color.blue(pixels[i]) + Color.blue(pixelarraytext[i])) / 2;
                   pixels[i] = Color.rgb(moyred, moygreen, moyblue);
               }
           }
       }
   }

    public static void setImage(Img imagebase){
        image = imagebase;
    }

    public static Img getImage(){
        return image;
    }

}




