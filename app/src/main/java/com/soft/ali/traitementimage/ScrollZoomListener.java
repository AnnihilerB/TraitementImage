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

    float curX = 0;
    float curY = 0;
    float posX, posY;
    float width;
    float height;
    Matrix saved = new Matrix();
    Matrix matrix = new Matrix();

    int mode;
    float currentDistance2Fingers;
    float midPointX, midPointY;

    /**
     * Overriding the onTouch method allows to handle zooming and scrolling on an image.
     * The method knows how many fingers are on the screen and so decides if the user wants to scroll or zoom.
     * The ACTION_MASK is needed here because when a event occurs, the ID of the event is included into the action.
     * ACTION_MASK allows us to get the ID of the action (e.g which finger presses the screen) by shifting bits.
     *
     * @param view        The view the method is used with.
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

        float newX, newY;
        float dX, dY;

        float values[] = new float[10];
        matrix.getValues(values);
        width = values[Constants.MATRIX_WIDTH_SCALE] * (imgView.getDrawable().getIntrinsicWidth());
        height = values[Constants.MATRIX_HEIGHT_SCALE] * (imgView.getDrawable().getIntrinsicHeight());

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            //One finger on the screen.
            case MotionEvent.ACTION_DOWN:
                mode = Constants.MODE_SCROLL;
                saved.set(matrix);
                curX = motionEvent.getX();
                curY = motionEvent.getY();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = Constants.MODE_ZOOM;
                currentDistance2Fingers = getDistance2Fingers(motionEvent);
                midPointX = getMidPointX(motionEvent);
                midPointY = getMidPointY(motionEvent);
                break;

            case MotionEvent.ACTION_UP:
                mode = Constants.MODE_NONE;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = Constants.MODE_NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == Constants.MODE_SCROLL) {
                    matrix.set(saved);
                    matrix.getValues(values);
                    posX = values[Constants.MATRIX_X_POSITION];
                    posY = values[Constants.MATRIX_Y_POSITION];
                    newX = motionEvent.getX();
                    newY = motionEvent.getY();
                    dX = newX - curX;
                    dY = newY - curY;
                    if (posX + dX > 0)
                        dX = -posX;
                    if (posY + dY > 0)
                        dY = -posY;
                    if (posX + dX + width < view.getWidth())
                        dX = view.getWidth() - posX - width;
                    if (posY + dY + height < view.getHeight())
                        dY = view.getHeight() - posY - height;
                    matrix.postTranslate(dX, dY);
                }
                if (mode == Constants.MODE_ZOOM){
                    float newDist2Fingers = getDistance2Fingers(motionEvent);
                    matrix.set(saved);
                    float scale = newDist2Fingers / currentDistance2Fingers;
                    matrix.postScale(scale, scale, midPointX, midPointY);
                }
                break;
        }
        imgView.setImageMatrix(matrix);
        return true;
    }

    private float getDistance2Fingers(MotionEvent event){
        float X = event.getX(0) - event.getX(1);
        float Y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(X * X + Y * Y);
    }

    private  float getMidPointX(MotionEvent event){
        float X = event.getX(0) - event.getX(1);
        return X/2;
    }

    private  float getMidPointY(MotionEvent event){
        float Y = event.getY(0) - event.getY(1);
        return Y/2;
    }

}

