package com.soft.ali.traitementimage;

import android.graphics.Color;

/**
 * Created by ali on 27/01/2017.
 */

public class ImgProcessing {

    private static Img image;

    /**
     * This method changes the hue of an image.
     * The user can choose the hue he wants from a color picker and the hue is change
     * accordingly to the hue he choosed.
     * The image is converted to HSV first then the hue is changed.
     */
    public static void colorize() {
        float hsv[] = new float[3];
        int pixels[] = image.getArraypixel();
        for (int i = 0; i < pixels.length; ++i) {

            Color.colorToHSV(pixels[i], hsv);
            hsv[Constants.HSV_HUE] = (float) 120;  // TODO: Work with the color picker.
            pixels[i] = Color.HSVToColor(hsv);
        }
    }

    /**
     * This method extends the contrast of the image.
     * To avoid blending wrong colors by working on separated RGB channels, the algorithm works on
     * the V channel of the HSV colorspace.
     * First, an histogram and a cumulative histogram are generated.
     * Each pixel is converted into HSV. As the V component is between 0 and 1, the component is
     * rescaled to be between 0 and 255.
     * Then we get the value of the V component in the cumulative histogram. Then, this value is
     * divided by the number of pixels in order to rescale it between 0 and 1.
     */
    public static void histogramEqualization(){
        Histogram hist = new Histogram();
        int pixels[] = image.getArraypixel();

        int channel = Constants.HSV_VIBRANCE;
        hist.generateHSVHistogram(pixels, channel);
        int nbPixels = hist.getNbPixels();

        float[] hsv = new float[3];

        for (int i = 0; i < pixels.length; i++){
            Color.colorToHSV(pixels[i], hsv);
            int val = (int)(hsv[channel] * 255); //Rescaling the value
            hsv[channel] = ((float) hist.getCumulativeHistogramValueAt(val) / (float)nbPixels);
            pixels[i] = Color.HSVToColor(hsv);
        }
    }

    public void extensionDynamique() {
        //La faire en HSV et ne pas toucher la teinte. Faire l'extension sur S puis repasser en RGB.
    }

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




