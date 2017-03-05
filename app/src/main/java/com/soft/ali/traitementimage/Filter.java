package com.soft.ali.traitementimage;

import android.widget.Toast;

/**
    This class create a two dimension array of floats which stores the values of the different filters (Average, Gauss, Sobel and Laplace) by calling the "set" methods.
    For create the class the user need to give a size filter.
    The filter array is a matrix 3*3 or more.
 */

public class Filter {

    private float filter [] [];
    private int sizefilter;

    public Filter (int nfilter){
        sizefilter=nfilter;
        filter = new float[sizefilter] [sizefilter];
    }

    public float[][] getFilter(){
        return filter;
    }

    public int getsizefilter(){
        return sizefilter;
    }

    /**
     A low pass filter for reducing noises in a picture, every value in the filter as the same number.
     */
    public void setAverage(){
        for(int i=0; i<sizefilter; i++){
            for(int j=0; j<sizefilter; j++){
                filter[i][j]=1/(float)(sizefilter*sizefilter);
            }
        }
    }

    /**
        First an indice array is initialise, each value match with the position of in x and y of the filter, after that, a calculation give a number for every boxes of the filter.
        These values are multiplied by a ratio, this ratio is the value need to get 1 in the boxes on the edge of the filter.
     * @param sigma is the standard deviation of the gaussian distribution.
     */
    public void setGauss(double sigma){
        int [][] matrixIndex = new int [sizefilter*sizefilter][2];
        int index=0;
        float totalValue=0;
        if(sizefilter==3){
            matrixIndex [0][0] = -1;
            matrixIndex [0][1] = -1;
            matrixIndex [1][0] = 0;
            matrixIndex [1][1] = -1;
            matrixIndex [2][0] = 1;
            matrixIndex [2][1] = -1;
            matrixIndex [3][0] = -1;
            matrixIndex [3][1] = 0;
            matrixIndex [4][0] = 0;
            matrixIndex [4][1] = 0;
            matrixIndex [5][0] = 1;
            matrixIndex [5][1] = 0;
            matrixIndex [6][0] = -1;
            matrixIndex [6][1] = 1;
            matrixIndex [7][0] = 0;
            matrixIndex [7][1] = 1;
            matrixIndex [8][0] = 1;
            matrixIndex [8][1] = 1;

            //Ratio is used to set the matrix corner value to one.
            float ratio = 0;
            float fraction = 1/(float)(2*Math.PI*sigma*sigma);

            for(int i=0; i<sizefilter;i++){
                for(int j=0; j<sizefilter; j++){

                    float exp = (float)Math.exp(-( (matrixIndex[index][0]*matrixIndex[index][0] +matrixIndex[index][1]*matrixIndex[index][1])  / ((2*sigma*sigma))     ));
                    float calculus = fraction *exp;

                    index++;
                    filter[i][j] = calculus;
                    totalValue=totalValue + filter [i][j];
                }
            }
            for(int i=0; i<sizefilter; i++){
                for(int j=0; j<sizefilter; j++){
                    filter[i][j] *=1/totalValue;
                }
            }
        }
    }

    public void setSobelVertical(){
        if(sizefilter == 3) {
            filter[0][0] = -1;
            filter[0][1] = 0;
            filter[0][2] = 1;
            filter[1][0] = -2;
            filter[1][1] = 0;
            filter[1][2] = 2;
            filter[2][0] = -1;
            filter[2][1] = 0;
            filter[2][2] = 1;
        }

        if(sizefilter == 5) {
            filter[0][0] = -1;
            filter[0][1] = -2;
            filter[0][2] = 0;
            filter[0][3] = 2;
            filter[0][4] = 1;
            filter[1][0] = -4;
            filter[1][1] = -8;
            filter[1][2] = 0;
            filter[1][3] = 8;
            filter[1][4] = 4;
            filter[2][0] = -6;
            filter[2][1] = -12;
            filter[2][2] = 0;
            filter[2][3] = 12;
            filter[2][4] = 6;
            filter[3][0] = -4;
            filter[3][1] = -8;
            filter[3][2] = 0;
            filter[3][3] = 8;
            filter[3][4] = 4;
            filter[4][0] = -1;
            filter[4][1] = -2;
            filter[4][2] = 0;
            filter[4][3] = 2;
            filter[4][4] = 1;
        }
    }

    public void setSobelHorizontal(){
        if(sizefilter == 3){
            filter[0][0] = -1;
            filter[0][1] = -2;
            filter[0][2] = -1;
            filter[1][0] = 0;
            filter[1][1] = 0;
            filter[1][2] = 0;
            filter[2][0] = 1;
            filter[2][1] = 2;
            filter[2][2] = 1;
        }
        if(sizefilter == 5){
            filter[0][0] = -1;
            filter[0][1] = -4;
            filter[0][2] = -6;
            filter[0][3] = -4;
            filter[0][4] = -1;
            filter[1][0] = -2;
            filter[1][1] = -8;
            filter[1][2] = -12;
            filter[1][3] = -8;
            filter[1][4] = -2;
            filter[2][0] = 0;
            filter[2][1] = 0;
            filter[2][2] = 0;
            filter[2][3] = 0;
            filter[2][4] = 0;
            filter[3][0] = 2;
            filter[3][1] = 8;
            filter[3][2] = 12;
            filter[3][3] = 8;
            filter[3][4] = 2;
            filter[4][0] = 1;
            filter[4][1] = 4;
            filter[4][2] = 6;
            filter[4][3] = 4;
            filter[4][4] = 1;
        }
    }

    public void setLaplace(){
        if(sizefilter==3) {
            setGauss(0.8);
            for (int i = 0; i < 5; i++)
                ImgProcessing.convolution(3, Constants.GAUSS);
            filter[0][0] = 0;
            filter[0][1] = 1;
            filter[0][2] = 0;
            filter[1][0] = 1;
            filter[1][1] = -4;
            filter[1][2] = 1;
            filter[2][0] = 0;
            filter[2][1] = 1;
            filter[2][2] = 0;
        }

    }

    public void setLaplace2(){
        if(sizefilter==3) {
            setGauss(0.8);
            for (int i = 0; i < 5; i++)
                ImgProcessing.convolution(3, Constants.GAUSS);
            filter[0][0] = 1;
            filter[0][1] = 1;
            filter[0][2] = 1;
            filter[1][0] = 1;
            filter[1][1] = -8;
            filter[1][2] = 1;
            filter[2][0] = 1;
            filter[2][1] = 1;
            filter[2][2] = 1;
        }
    }
}
