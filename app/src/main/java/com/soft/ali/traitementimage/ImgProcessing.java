package com.soft.ali.traitementimage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.soft.ali.traitementimage.histogram.CumulativeHistogram;
import com.soft.ali.traitementimage.histogram.Histogram;

/**
 * Created by ali on 27/01/2017.
 */

public class ImgProcessing {

    private static Img image;
    static  Histogram hist;
    static CumulativeHistogram cumulativeHistogram;
    static LUT lut;
    static int []pixels;


    /**
     * This method changes the hue of an image.
     * The user can choose the hue he wants from a color picker and the hue is change
     * accordingly to the hue he choosed.
     * The image is converted to HSV first then the hue is changed.
     */
    public static void colorize(int chosencolor, int lower, int upper) {
        float hsv[] = new float[3];
        int pixels[] = image.getArrayPixel();
        for (int i = lower; i < upper; ++i) {
            Color.colorToHSV(pixels[i], hsv);
            hsv[Constants.HSV_HUE] = (float) chosencolor;
            pixels[i] = Color.HSVToColor(hsv);
        }
    }

    /**
     * This method extends the contrast of the image.
     * To avoid blending wrong colors by working on separated RGB channels, the algorithm works on
     * the V channel of the HSV colorspace.
     * First, an histogram and a cumulative histogram are generated.
     * Each pixel is converted into HSV. As the V component is between 0 and 1, the component is
     * rescaled to fit between 0 and 255.
     * Then we get the value of the V component in the cumulative histogram. Then, this value is
     * divided by the number of pixels in order to rescale it between 0 and 1.
     */
    public static void histogramEqualization(int lower, int upper){

        int channel = Constants.HSV_VIBRANCE;

        float nbPixels = (float)hist.getNbPixels();
        float[] hsv = new float[3];

        for (int i = lower; i < upper; i++){
            Color.colorToHSV(pixels[i], hsv);
            int val = (int)(hsv[channel] * 255); //Rescaling the value
            hsv[channel] = ((float) cumulativeHistogram.getCumulativeHistogramValueAt(val) / nbPixels);
            pixels[i] = Color.HSVToColor(hsv);
        }
    }

    public static void generateHist(){
        pixels = image.getArrayPixel();

        hist = new Histogram();
        cumulativeHistogram = new CumulativeHistogram();

        hist.generateHSVHistogram(pixels, Constants.HSV_VIBRANCE);
        cumulativeHistogram.generateCumulativeHistogram(hist.getHistogram());

    }

    /**
     * Extending the image dynamism via a LUT.
     * Each pixel is rescaled between 0 and 255 in order ro fit the LUT.
     * A new pixel value is gathered from the LUT and scaled between 0 and 1.
     * The extension is processed via the HSV colorspace.
     */
    public static void extendDynamism(int lower, int upper) {
        int channel = Constants.HSV_VIBRANCE;
        int pixels[] = image.getArrayPixel();
        float[] hsv = new float[3];

        for (int i = lower; i <upper; i++) {
            Color.colorToHSV(pixels[i],hsv);
            int value = (int)(hsv[channel] *255);
            float res = (lut.getValueAt(value)) / 255;
            hsv[channel] = res;
            pixels[i]= Color.HSVToColor(hsv);
        }
    }

    public static void generateDynamismLUT(){
        LUT lut = new LUT();
        lut.generateHSV(image);
    }



    public static void toGray(int lower, int upper){
        int red, green, blue;
        int rgb, total;
        int pixels[] = image.getArrayPixel();

        for (int i = lower; i < upper; i++) {
            rgb = pixels[i];
            red = ((Color.red(rgb)*3)/10);
            green = ((Color.green(rgb)*59)/100);
            blue = ((Color.blue(rgb)*11)/100);
            total = red + green + blue;
            rgb = Color.rgb(total, total, total);
            pixels[i]=rgb;
        }
    }
    /**
     The user gives as parameter the size of the size of the filter and its type (average, gaussian...).
     A Filter object is created, after that, in function of the type, a "set" method is called to fill the filter. Next the method "calculConvolution()" applied the filter on the picture.
     */
    public static void convolution(int n, int typeFilter) {
        Filter filter = new Filter(n);

        if(typeFilter == Constants.AVERAGE){
            filter.setAverage();
            calculConvolution(filter.getFilter(), filter.getSizeFilter());
        }

        if(typeFilter == Constants.GAUSS){
            double sigma = Constants.SIGMA;
            filter.setGauss(sigma);
            calculConvolution(filter.getFilter(), filter.getSizeFilter());
        }

        if(typeFilter == Constants.SOBEL){
            filter.setSobelHorizontal();
            calculConvolution(filter.getFilter(), filter.getSizeFilter());
            int[] pixelHorizontal= image.getArrayPixel().clone();
            filter.setSobelVertical();
            calculConvolution(filter.getFilter(), filter.getSizeFilter());
            int[] pixelVertical = image.getArrayPixel().clone();

            int[]pixels = image.getArrayPixel();
            int r,g,b;
            for (int i =0; i < pixelHorizontal.length; i++){
                r = (int) (Math.sqrt(Math.pow(Color.red(pixelHorizontal[i]),2) + Math.pow(Color.red(pixelVertical[i]), 2)));
                g = (int) (Math.sqrt(Math.pow(Color.green (pixelHorizontal[i]),2) + Math.pow(Color.green(pixelVertical[i]), 2)));
                b = (int) (Math.sqrt(Math.pow(Color.blue(pixelHorizontal[i]),2) + Math.pow(Color.blue(pixelVertical[i]), 2)));
                pixels[i] = Color.rgb(r, g ,b);
            }

        }

        if(typeFilter == Constants.LAPLACE){
            filter.setLaplace();
            calculConvolution(filter.getFilter(), filter.getSizeFilter());
        }

        if(typeFilter == Constants.LAPLACE2){
            filter.setLaplace2();
            calculConvolution(filter.getFilter(), filter.getSizeFilter());
        }
    }

    private static int floorMod(int a, int b) {
        int r = a % b;
        if (r < 0)
            r += b;
        return r;
    }

    /**
     * This method applied the filter on the chosen image, for the moment only the 3*3 matrix are functionnal.
     * The edge of the picture are not processed.
     * @param filterMatrix the filter to apply.
     * @param sizeFilter the size of the filer (odd number).
     */
    private static void calculConvolution(float [][] filterMatrix, int sizeFilter) {
        int pixels[] = image.getArrayPixel();
        int originalPixels[] = pixels.clone();

        int width = image.getWidth();
        int height = image.getHeight();

        int r, g, b;

        // Calculates the maximum and minimum output of the convolution with the given mask
        float max_value = 0;
        float min_value = 0;
        for(int i = 0; i < filterMatrix.length; i++) {
            for (int j = 0; j < filterMatrix.length; j++) {
                if (filterMatrix[i][j] >= 0)
                    max_value += filterMatrix[i][j] * 255;
                else
                    min_value += filterMatrix[i][j] * 255;
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = 0;

                for (int i = 0; i < filterMatrix.length; i++) {
                    for (int j = 0; j < filterMatrix.length; j++) {
                        int yPrime = floorMod(y + i - (pixels.length - 1) / 2, height);
                        int xPrime = floorMod(x + j - (pixels.length - 1) / 2, width);
                        value += (0x000000FF & originalPixels[yPrime * width + xPrime]) * filterMatrix[i][j];
                    }
                }

                // Checks if the output is not in [0,255] and uses the appropriate bijection to fix it
                if (value > 255 || value < 0)
                    value = (int) ((value - min_value)/(max_value - min_value)) * 255;

                pixels[y * width + x] = 0xFF000000 | (value << 16) | (value << 8) | value;
            }
        }

    }
    /**
     * This function increase the brightness value of all pixels of a picture by an arbitrary value.
     */
    public static void overexposure (int lower, int upper) {

        float hsv[] = new float[3];
        int pixels[] = image.getArrayPixel();
        for(int i=lower; i<upper; i++){
            Color.colorToHSV(pixels[i], hsv);
            hsv[Constants.HSV_VIBRANCE] = (float) (hsv[Constants.HSV_VIBRANCE] + 0.20);
            pixels[i] = Color.HSVToColor(hsv);
        }
    }

    /**
     * This method isolate a color of an image.
     * The user can choose the color he wants from the value picker.
     * @param colorValue color to isolate.
     */
    public static void isolate(int colorValue, int lower, int upper) {

        int limit = 150;

        int canalgrey;
        double valred = 0.3;
        double valgreen = 0.59;
        double valblue = 0.11;
        int distance;

        int pixels[] = image.getArrayPixel();

        for (int i = lower; i < upper; i++) {
            distance = (int) (Math.sqrt(Math.pow((Color.red(colorValue) - Color.red(pixels[i])), 2) + Math.pow((Color.green(colorValue) - Color.green(pixels[i])), 2) + Math.pow((Color.blue(colorValue) - Color.blue(pixels[i])), 2)));
            if (distance >= limit) {
                canalgrey = (int) ((Color.red(pixels[i]) * valred) + (Color.green(pixels[i]) * valgreen) + (Color.blue(pixels[i]) * valblue));
                pixels[i] = Color.rgb(canalgrey, canalgrey, canalgrey);
            }

        }
    }

    /**
     * Gives the sepia effect to the image.
     */
    public static void sepia(int lower, int upper){

        int pixels[] = image.getArrayPixel();
        double valred;
        double valgreen;
        double valblue;
        int canalred;
        int canalgreen;
        int canalblue;

        for(int i=lower; i<upper; i++){
            valred = 0.393;
            valgreen = 0.769;
            valblue = 0.189;
            canalred = (int) Math.min(255, ((Color.red(pixels[i])*valred)+(Color.green(pixels[i])*valgreen)+(Color.blue(pixels[i])*valblue)));

            valred = 0.349;
            valgreen = 0.686;
            valblue = 0.168;
            canalgreen = (int) Math.min(255, ((Color.red(pixels[i])*valred)+(Color.green(pixels[i])*valgreen)+(Color.blue(pixels[i])*valblue)));

            valred = 0.272;
            valgreen = 0.534;
            valblue = 0.131;
            canalblue = (int) Math.min(255, ((Color.red(pixels[i])*valred)+(Color.green(pixels[i])*valgreen)+(Color.blue(pixels[i])*valblue)));

            pixels[i] = Color.rgb(canalred, canalgreen, canalblue);
        }
    }

    /**
     *  Fusion between two pictures, one is a white picture with black text. The text image need to be smaller or equal to the other one.
     * When a black pixel is detected in the array of the text picture, an average value of the two pixel is done.
     * @param bitmapText Ht eimage to be merged with.
     */
    public static void fusion (Bitmap bitmapText) {

        int widthtext = bitmapText.getWidth();
        int heighttext = bitmapText.getHeight();
        int moyred, moygreen, moyblue;
        int pixels[] = image.getArrayPixel();
        int pixelarraytext[] = new int[widthtext * heighttext];

        bitmapText.getPixels(pixelarraytext,0,widthtext,0,0,widthtext,heighttext);

        if (pixelarraytext.length <= pixels.length) {
            for (int i = 0; i < pixelarraytext.length; i++) {
                if (pixelarraytext[i] == Color.BLACK) {
                    moyred = (Color.red(pixels[i]) + Color.red(pixelarraytext[i])) / 2;
                    moygreen = (Color.green(pixels[i]) + Color.green(pixelarraytext[i])) / 2;
                    moyblue = (Color.blue(pixels[i]) + Color.blue(pixelarraytext[i])) / 2;
                    pixels[i] = Color.rgb(moyred, moygreen, moyblue);
                }
            }
        }
    }

    /**
     * Hide a picture in another one ,we supposed both pics have same size
     * @param imgToHide
     */
    public static void hideImageToAnother(Img imgToHide){
        int red1, green1, blue1;
        int red2, green2, blue2;
        //variable to catch binary changes from both color rgb pictures
        String binaryResult;
        // array pixels of both pics : current pic and pic to hide
        int[] pixels1 = image.getArrayPixel();
        int[] pixels2 = imgToHide.getArrayPixel();
        for(int i=0; i< pixels1.length; i++) {
            //getting rgb colors from both pics
            red1 = Color.red(pixels1[i]);
            green1 = Color.green(pixels1[i]);
            blue1 = Color.blue(pixels1[i]);

            red2 = Color.red(pixels2[i]);
            green2 = Color.green(pixels2[i]);
            blue2 = Color.blue(pixels2[i]);
            //getting fourth first byte from binary value of rgb colors and concat between both pics rgb colors
            //put results in rgb arg of current pic
            binaryResult = getFirstsBinaryValue(red1);
            binaryResult.concat(getFirstsBinaryValue(red2));
            red1 = Integer.parseInt(binaryResult, 2);
            binaryResult = getFirstsBinaryValue(green1);
            binaryResult.concat(getFirstsBinaryValue(green2));
            green1 = Integer.parseInt(binaryResult, 2);
            binaryResult = getFirstsBinaryValue(blue1);
            binaryResult.concat(getFirstsBinaryValue(blue2));
            blue1 = Integer.parseInt(binaryResult, 2);


            //Evaluating new Rgb color of current pic pixel
            pixels1[i] = Color.rgb(red1, green1, blue1);
        }
    }

    /**
     * Get fourth first byte of binary string of a rgb arg
     * @param rgbArg
     * @return The fourth byte of a String
     */
    public static String getFirstsBinaryValue(int rgbArg){
        String binaryString =Integer.toBinaryString(rgbArg);
        return binaryString.substring(0,3);
    }

    /**
     * Setting the image to process.
     * @param imagebase image to process.
     */
    public static void setImage(Img imagebase){
        image = imagebase;
    }

    /**
     * Reverse the color of a grayscale image.
     * Constructs a LUT containing the reversed colors and then changes every pixels.
     */
    public static void reverseImg(){
        ImgProcessing.toGray(0,  image.getWidth() * image.getHeight());
        int pixels[] = image.getArrayPixel();

        LUT reversedColors = new LUT();
        reversedColors.generateReversedLUT();

        for(int i = 0; i < pixels.length; i ++){
            int value  = (int)reversedColors.getValueAt(Color.red(pixels[i]));
            pixels[i] = Color.rgb(value, value, value);
        }
    }

    /**
     * Transform the image to simulate a sketch effect.
     * The image is transformed into grayscale.
     * A copy of this image is reversed and a Gaussian filter is applied.
     * The two images are blend using the Linear dodge process. (http://www.wikiwand.com/en/Blend_modes#/Dodge_and_burn).
     */
    public static void sketchImage(){

        ImgProcessing.toGray(0, image.getWidth() * image.getHeight());
        int[] originalPixels = image.getArrayPixel().clone();

        ImgProcessing.reverseImg();
        ImgProcessing.convolution(3, Constants.GAUSS);


        int reversedPixels[]  = image.getArrayPixel();

        for (int i = 0; i < originalPixels.length; i++){
            int value = (Color.red(reversedPixels[i]) + Color.red(originalPixels[i]));
            if (value > 255){
                value  = 120;
            }
            reversedPixels[i] = Color.rgb(value, value, value);
        }
    }

    /**
     * Adjust the contrast of the current image displayed on screen.
     * The contrast method has been found here :
     * http://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-5-contrast-adjustment/
     * @param contrast The contrast value the user wants..
     */
    public static void contrastAdjust(int[] originalPixels, int contrast, int lower, int upper){
        int pixels[] = image.getArrayPixel();

        float factor = (259 * (contrast + 255)) /(float) (255 * (259 - contrast));

        for (int i = lower; i < upper; i++){
            int color = originalPixels[i];
            int r = Math.round(truncate(factor * (Color.red(color)  - 128) + 128));
            int g = Math.round(truncate(factor * (Color.green(color)  - 128) + 128));
            int b = Math.round(truncate(factor * (Color.blue(color)  - 128) + 128));
            pixels[i] = Color.rgb(r,g,b);
        }
    }

    /**
     * Truncate a color value in order to fit between 0 and 255.
     * @param val the value to truncate
     * @return the truncate value or the initial value if no truncating needed.
     */
    private static float truncate (float val){
        if (val > 255)
            return 255;
        if (val < 0)
            return 0;
        return val;
    }
}






