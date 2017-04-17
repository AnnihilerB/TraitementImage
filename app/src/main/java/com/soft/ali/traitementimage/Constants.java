package com.soft.ali.traitementimage;

/**
 * Created by ali on 11/02/2017.
 */

public class Constants {

    public static final int MATRIX_X_POSITION = 2;
    public static final int MATRIX_Y_POSITION = 5;
    public static final int MATRIX_WIDTH_SCALE = 0;
    public static final int MATRIX_HEIGHT_SCALE= 4;

    public static final int LOAD_PERMISSIONS = 41;
    public static final int CAMERA_PERMISSIONS = 42;
    public static final int WRITE_PERMISSIONS = 43;

    //Convolution constants
    public static final int AVERAGE = 0;
    public static final int GAUSS = 1;
    public static final double SIGMA = 0.8;
    public static final int SOBEL = 2;
    public static final int LAPLACE = 3;
    public static final int LAPLACE2 = 4;

    //Activity constants
    public static final int LOAD_IMAGE = 1;
    public static final int REQUEST_CAPTURE = 2;
    public static  final int WRITE_IMAGE = 3;
    public static  final int PICKER_VALUE = 4;

    //Int constants
    public static final int NBCOLORS = 256;

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
    public static final int MODE_ZOOM=21;
    public static final int MODE_SCROLL =22;
    public static final int MODE_NONE = 20;

    //Gallery Choice mode constants
    public static final int GALLERY_NORMAL = 60;
    public static final int GALLERY_SECOND_IMAGE =61;

}
