package com.soft.ali.traitementimage;

/**
 * Created by ali on 11/02/2017.
 */

public class Constants {

    //Convolution constants
    public static final int AVERAGE = 0;
    public static final int GAUSS = 1;
    public static final int SOBEL = 2;
    public static final int LAPLACE = 3;
    public static final int LAPLACE2 = 4;

    //Activity constants
    public static int LOAD_IMAGE = 1;
    public static final int REQUEST_CAPTURE = 2;
    public static  final int WRITE_IMAGE = 3;

    //Int constants
    public static final int NBCOLORS = 256;

    //Log string constants
    public static final String POINTER = "PTR";

    //HSV constants
    public  static final int HSV_HUE = 0;
    public  static final int HSV_SATURATION = 1;
    public  static final int HSV_VIBRANCE = 2;

    //Error constants
    public static final String ERROR_WRITING_IMAGE = "ERRWR";
    public final String ERROR_GETTING_IMAGE = "ERRGET";

    //Camera constants
    public static final String FILE_NAME = "imgTmp";
    public static final String EXTENSION = ".png";

    //Mode Action Moves constants
    public static final int MODE_NONE=0;
    public static final int MODE_ZOOM=1;
    public static final int MODE_DRAG =2;

}
