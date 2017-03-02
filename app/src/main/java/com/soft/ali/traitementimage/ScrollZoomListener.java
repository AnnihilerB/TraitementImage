package com.soft.ali.traitementimage;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by ali on 28/01/2017.
 */


/**
 * This class implements a new OnTouchListener.
 * This listener allows us to control the events.
 *  The listener is made by overriding the onTouch method.
 */
public class ScrollZoomListener implements View.OnTouchListener {

    //Postion of the first finger
    float curX = 0;
    float curY = 0;

    //On-screen location of the matrix
    float posX, posY;
    //Width and height of the bitmap.
    float width;
    float height;
    //Saved matrix allows us to get the basic values.
    Matrix savedMatrix = new Matrix();
    Matrix matrix = new Matrix();

    int mode;
    //Distance when 2 fingers are on screen.
    float currentDistance2Fingers;

    float midPointX, midPointY;

    /**
     * Overriding the onTouch method allows to handle zooming and scrolling on an image.
     * The method knows how many fingers are on the screen and so decides if the user wants to scroll or zoom.
     * The event is processed by a matrix which allows us to zoom or scroll efficiently.
     * An array of float stores all the different values from the matrix such as the scale factor or the matrix position on screen.
     * @param view The view the method is used with.
     * @param motionEvent the event.
     * @return true when the event is done.
     */

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ImgView imgView = (ImgView) view;
        // index is the index of the first finger in the event queue.
        int index = motionEvent.getActionIndex();
        //Getting the IDs of the others fingers.
        int pointerID = motionEvent.getPointerId(index);

        //New finger location when dragging
        float newX, newY;
        //Distance we are moving the matrix by.
        float dX, dY;

        float values[] = new float[10];
        matrix.getValues(values);

        width = values[Constants.MATRIX_WIDTH_SCALE] * (imgView.getDrawable().getIntrinsicWidth());
        height = values[Constants.MATRIX_HEIGHT_SCALE] * (imgView.getDrawable().getIntrinsicHeight());

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            //One finger on the screen.
            case MotionEvent.ACTION_DOWN:
                mode = Constants.MODE_SCROLL;
                savedMatrix.set(matrix);
                curX = motionEvent.getX();
                curY = motionEvent.getY();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = Constants.MODE_ZOOM;
                currentDistance2Fingers = getDistance2Fingers(motionEvent);
                //Getting midpoint between fingers.
                midPointX = getMidPointX(motionEvent);
                midPointY = getMidPointY(motionEvent);
                break;

            //Avoids an OOB exception when lifting a finger
            case MotionEvent.ACTION_UP:
                mode = Constants.MODE_NONE;
                break;

            //Avoids an OOB exception when lifting a finger
            case MotionEvent.ACTION_POINTER_UP:
                mode = Constants.MODE_NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == Constants.MODE_SCROLL) {
                    matrix.set(savedMatrix);
                    matrix.getValues(values);
                    //Getting the matrix position
                    posX = values[Constants.MATRIX_X_POSITION];
                    posY = values[Constants.MATRIX_Y_POSITION];
                    //Finger coordinates after dragging
                    newX = motionEvent.getX();
                    newY = motionEvent.getY();
                    //Distance the matrix has to move.
                    dX = newX - curX;
                    dY = newY - curY;
                    //Detecting edges
                    if (posX + dX > 0) //Left
                        dX = -posX;
                    if (posY + dY > 0)//Top
                        dY = -posY;
                    if (posX + dX + width < view.getWidth())//Right
                        dX = view.getWidth() - posX - width;
                    if (posY + dY + height < view.getHeight())//Bottom
                        dY = view.getHeight() - posY - height;
                    //Making the translation.
                    matrix.postTranslate(dX, dY);
                }
                if (mode == Constants.MODE_ZOOM){
                    //Distance after pinching.
                    float newDist2Fingers = getDistance2Fingers(motionEvent);
                    matrix.set(savedMatrix);
                    //Calculating the scale we need to render the image.
                    float scale = newDist2Fingers / currentDistance2Fingers;
                    matrix.postScale(scale, scale, midPointX, midPointY);
                }
                break;
        }
        //Updating the view
        imgView.setImageMatrix(matrix);
        return true;
    }

    /**
     * Method calculating the euclidian distance between two fingers e.g. two points.
     * @param event the event whe have to process.
     * @return euclidian distance between the fingers.
     */
    private float getDistance2Fingers(MotionEvent event){
        float X = event.getX(0) - event.getX(1);
        float Y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(X * X + Y * Y);
    }

    /**
     * Calculation of the abscissa of the midPoint between two fingers.
     * @param event the event whe have to process.
     * @return abscissa of the midpoint.
     */
    private  float getMidPointX(MotionEvent event){
        float X = event.getX(0) - event.getX(1);
        return X/2;
    }

    /**
     * Calculation of the ordinate of the midPoint between two fingers.
     * @param event the event whe have to process.
     * @return ordinate of the midpoint.
     */
    private  float getMidPointY(MotionEvent event){
        float Y = event.getY(0) - event.getY(1);
        return Y/2;
    }

}

