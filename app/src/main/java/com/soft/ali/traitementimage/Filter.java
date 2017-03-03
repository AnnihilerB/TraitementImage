package com.soft.ali.traitementimage;

import android.util.Log;

/**
 * Created by Valentin on 17/02/2017.
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

    public void setAverage(){
        for(int i=0; i<sizefilter; i++){
            for(int j=0; j<sizefilter; j++){
                filter[i][j]=1/(float)(sizefilter*sizefilter);
            }
        }
    }

    public void setGauss(double sigma){
        int [][] indice = new int [sizefilter*sizefilter][2];
        int numIndice=0;
        int totalValue=0;
        if(sizefilter==3){
            indice [0][0] = -1;
            indice [0][1] = -1;
            indice [1][0] = 0;
            indice [1][1] = -1;
            indice [2][0] = 1;
            indice [2][1] = -1;
            indice [3][0] = -1;
            indice [3][1] = 0;
            indice [4][0] = 0;
            indice [4][1] = 0;
            indice [5][0] = 1;
            indice [5][1] = 0;
            indice [6][0] = -1;
            indice [6][1] = 1;
            indice [7][0] = 0;
            indice [7][1] = 1;
            indice [8][0] = 1;
            indice [8][1] = 1;

            float ratio = 0;
            float fraction = 1/(float)(2*Math.PI*sigma*sigma);

            for(int i=0; i<sizefilter;i++){
                for(int j=0; j<sizefilter; j++){

                    float exp = (float)Math.exp(-( (indice[numIndice][0]*indice[numIndice][0] +indice[numIndice][1]*indice[numIndice][1])  / ((2*sigma*sigma))     ));
                    float calcul = fraction *exp;

                    if (i == 0 && j == 0){
                        ratio = 1/calcul;
                    }
                    filter[i][j] = calcul;
                    numIndice++;
                    filter[i][j] = Math.round(filter[i][j]* ratio);
                    totalValue=totalValue + Math.round(filter [i][j]);
                }
            }
            for(int i=0; i<sizefilter; i++){
                for(int j=0; j<sizefilter; j++){
                    filter[i][j] *=1/(float)(totalValue);
                }
            }
        }
    }

    public void setSobelVertical(){
        filter[0][0]= -1;
        filter[0][1]= 0;
        filter[0][2]= 1;
        filter[1][0]= -2;
        filter[1][1]= 0;
        filter[1][2]= 2;
        filter[2][0]= -1;
        filter[2][1]= 0;
        filter[2][2]= 1;
    }

    public void setSobelHorizontal(){
        filter[0][0]= -1;
        filter[0][1]= -2;
        filter[0][2]= -1;
        filter[1][0]= 0;
        filter[1][1]= 0;
        filter[1][2]= 0;
        filter[2][0]= 1;
        filter[2][1]= 2;
        filter[2][2]= 1;
    }

    public void setLaplace(){
        setGauss(0.8);
        for (int i = 0; i <5; i++)
            ImgProcessing.convolution(3, Constants.GAUSS);
        filter[0][0]= 0;
        filter[0][1]= 1;
        filter[0][2]= 0;
        filter[1][0]= 1;
        filter[1][1]= -4;
        filter[1][2]= 1;
        filter[2][0]= 0;
        filter[2][1]= 1;
        filter[2][2]= 0;
    }

    public void setLaplace2(){
        setGauss(0.8);
        for (int i = 0; i <5; i++)
            ImgProcessing.convolution(3, Constants.GAUSS);
        filter[0][0]= 1;
        filter[0][1]= 1;
        filter[0][2]= 1;
        filter[1][0]= 1;
        filter[1][1]= -8;
        filter[1][2]= 1;
        filter[2][0]= 1;
        filter[2][1]= 1;
        filter[2][2]= 1;
    }
}
