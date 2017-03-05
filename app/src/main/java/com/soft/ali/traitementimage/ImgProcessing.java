package com.soft.ali.traitementimage;

import android.graphics.Bitmap;
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
    public static void colorize(int chosencolor) {
        float hsv[] = new float[3];
        int pixels[] = image.getArraypixel();
        for (int i = 0; i < pixels.length; ++i) {

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


    /**
     * Extending the image dynamism via a LUT.
     * Each pixel is rescaled between 0 and 255 in order ro fit the LUT.
     * A new pixel value is gathered from the LUT and scaled between 0 and 1.
     * The extension is processed via the HSV colorspace.
     */
    public static void extendDynamism() {
        int channel = Constants.HSV_VIBRANCE;
        int pixels[] = image.getArraypixel();
        float[] hsv = new float[3];
        LUT lut = new LUT();
        lut.generateHSV(image);
        for (int i = 0; i <pixels.length; i++) {
            Color.colorToHSV(pixels[i],hsv);
            int value = (int)(hsv[channel] *255);
            float res = (lut.getValueAt(value)) / 255;
            hsv[channel] = res;
            pixels[i]= Color.HSVToColor(hsv);
        }
    }



    public static void toGray(){
        int red, green, blue;
        int rgb, total;
        int pixels[] = image.getArraypixel();

        for (int i = 0; i < pixels.length; i++) {
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
            calculConvolution(filter.getFilter(), filter.getsizefilter());
        }

        if(typeFilter == Constants.GAUSS){
            double sigma = 0.8;
            filter.setGauss(sigma);
            calculConvolution(filter.getFilter(), filter.getsizefilter());
        }

        if(typeFilter == Constants.SOBEL){
            filter.setSobelHorizontal();
            calculConvolution(filter.getFilter(), filter.getsizefilter());
            int[] pixelHorizontal= image.getArraypixel().clone();
            filter.setSobelVertical();
            calculConvolution(filter.getFilter(), filter.getsizefilter());
            int[] pixelVertical = image.getArraypixel().clone();

            int[]pixels = image.getArraypixel();
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
            calculConvolution(filter.getFilter(), filter.getsizefilter());
        }

        if(typeFilter == Constants.LAPLACE2){
            filter.setLaplace2();
            calculConvolution(filter.getFilter(), filter.getsizefilter());
        }
    }

    /**
     * This method applied the filter on the chosen image, for the moment only the 3*3 matrix are functionnal.
     * The edge of the picture are not processed.
     * @param filtermatrix the filter to apply.
     * @param sizefilter the size of the filer (odd number).
     */
    private static void calculConvolution(float [][] filtermatrix, int sizefilter) {
        int pixels[] = image.getArraypixel();
        int originalpixels[] = pixels.clone();

        int width = image.getWidth();
        int height = image.getHeight();
        //Index used to avoid edging pixels.
        int index = width + 1;

        int r, g, b;

        //This loop avoids to process the edge of the image.
        //The filter is applied on each RGB channel separately.
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                r = (int) ((Color.red(originalpixels[index - 1 - width]) * filtermatrix[0][0])
                        + (Color.red(originalpixels[index - width]) * filtermatrix[0][1])
                        + (Color.red(originalpixels[index + 1 - width]) * filtermatrix[0][2])
                        + (Color.red(originalpixels[index - 1]) * filtermatrix[1][0])
                        + (Color.red(originalpixels[index]) * filtermatrix[1][1])
                        + (Color.red(originalpixels[index + 1]) * filtermatrix[1][2])
                        + (Color.red(originalpixels[index - 1 + width]) * filtermatrix[2][0])
                        + (Color.red(originalpixels[index + width]) * filtermatrix[2][1])
                        + (Color.red(originalpixels[index + 1 + width])) * filtermatrix[2][2]);
                g = (int) ((Color.green(originalpixels[index - 1 - width]) * filtermatrix[0][0])
                        + (Color.green(originalpixels[index - width]) * filtermatrix[0][1])
                        + (Color.green(originalpixels[index + 1 - width]) * filtermatrix[0][2])
                        + (Color.green(originalpixels[index - 1]) * filtermatrix[1][0])
                        + (Color.green(originalpixels[index]) * filtermatrix[1][1])
                        + (Color.green(originalpixels[index + 1]) * filtermatrix[1][2])
                        + (Color.green(originalpixels[index - 1 + width]) * filtermatrix[2][0])
                        + (Color.green(originalpixels[index + width]) * filtermatrix[2][1])
                        + (Color.green(originalpixels[index + 1 + width])) * filtermatrix[2][2]);
                b = (int) ((Color.blue(originalpixels[index - 1 - width]) * filtermatrix[0][0])
                        + (Color.blue(originalpixels[index - width]) * filtermatrix[0][1])
                        + (Color.blue(originalpixels[index + 1 - width]) * filtermatrix[0][2])
                        + (Color.blue(originalpixels[index - 1]) * filtermatrix[1][0])
                        + (Color.blue(originalpixels[index]) * filtermatrix[1][1])
                        + (Color.blue(originalpixels[index + 1]) * filtermatrix[1][2])
                        + (Color.blue(originalpixels[index - 1 + width]) * filtermatrix[2][0])
                        + (Color.blue(originalpixels[index + width]) * filtermatrix[2][1])
                        + (Color.blue(originalpixels[index + 1 + width])) * filtermatrix[2][2]);

                pixels[index] = Color.rgb(r, g, b);
                index++;
            }
            //Going to the next "line" of the image
            index += 2;
        }
    }
    /**
     * This function increase the brightness value of all pixels of a picture by an arbitrary value.
     */
    public static void overexposure () {

        float hsv[] = new float[3];
        int pixels[] = image.getArraypixel();
        for(int i=0; i<pixels.length; i++){
            Color.colorToHSV(pixels[i], hsv);
            hsv[2] = (float) (hsv[2] + 0.20);
            pixels[i] = Color.HSVToColor(hsv);
        }
    }

    /**
     * This method isolate a color of an image.
     * The user can choose the color he wants from the value picker.
     * @param colorValue color to isolate.
     */
    public static void isolate(int colorValue) {

        int limit = 150;

        int canalgrey;
        double valred = 0.3;
        double valgreen = 0.59;
        double valblue = 0.11;
        int distance;

        int pixels[] = image.getArraypixel();

        for (int i = 0; i < pixels.length; i++) {
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
    public static void sepia(){

        int pixels[] = image.getArraypixel();
        double valred;
        double valgreen;
        double valblue;
        int canalred;
        int canalgreen;
        int canalblue;

        for(int i=0; i<pixels.length; i++){
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
        int pixels[] = image.getArraypixel();
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
        int[] pixels1 = image.getArraypixel();
        int[] pixels2 = imgToHide.getArraypixel();
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
     * @return
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

}




