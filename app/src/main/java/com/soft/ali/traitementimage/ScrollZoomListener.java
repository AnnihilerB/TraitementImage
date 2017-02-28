package com.soft.ali.traitementimage;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
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
    Rect rectangleImageView;
    Rect drawableRectangle = new Rect();
    boolean intersection;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    /**
     * Overriding the onTouch method allows to handle zooming and scrolling on an image.
     * The method knows how many fingers are on the screen and so decides if the user wants to scroll or zoom.
     * The ACTION_MASK is needed here because when a event occurs, the ID of the event is included into the action.
     * ACTION_MASK allows us to get the ID of the action (e.g which finger presses the screen) by shifting bits.
     * @param view The view the method is used with.
     * @param motionEvent the event.
     * @return true when the event is done.
     */

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        // index is the index of the first finger in the event queue.
        int index = motionEvent.getActionIndex();
        //Getting the IDs of the others fingers.
        int pointerID = motionEvent.getPointerId(index);

        //Cooridnates of the first finger ont the screen
        float newX, newY;

        float oldDist = 1f;
        //Coordinates of the moving finger
        int[] location = new int[2];
        //Coordinates of mid point fingers scale
        float[] mid = new float[2];
        mid[0]=0;
        mid[1]=0;
        ImgView imgV = (ImgView) view;

        int mode=Constants.MODE_NONE;
        float mCurrentScale = 1.0f;

        //not used but can be if we need make more methods for some fingers use
        int pointCnt = motionEvent.getPointerCount();

        rectangleImageView = new Rect(location[0], location[1], view.getWidth(), view.getHeight());
        view.getLocationOnScreen(location);



        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            //One finger on the screen.
            case MotionEvent.ACTION_DOWN:
                Log.i(Constants.POINTER, "One finger");
                curX = motionEvent.getX();
                curY = motionEvent.getY();
                savedMatrix.set(matrix);
                view.getLocalVisibleRect(drawableRectangle);
                intersection = drawableRectangle.intersect(rectangleImageView);
                break;
            case MotionEvent.ACTION_MOVE:
                if (drawableRectangle.intersect(rectangleImageView)) {
                    newX = motionEvent.getX();
                    newY = motionEvent.getY();
                    view.scrollBy((int) (curX - newX), (int) (curY - newY));
                    curX = newX;
                    curY = newY;
                    Log.i("PTR", "iv left : " + String.valueOf(location[0]) + "exact center : " + String.valueOf((int)drawableRectangle.exactCenterX()) + "iv right : " + String.valueOf(view.getWidth()));
                }
                else if (mode== Constants.MODE_ZOOM){
                    float newDist = spacing(motionEvent);
                    Log.d("TAG", "newDist=" + newDist);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;

                        // check scale is not too big or two small
                        float newScale = scale * mCurrentScale;
                        if (newScale > 10) {
                            return false;
                        } else if (newScale < 0.1) {
                            return false;
                        }
                        mCurrentScale = newScale;

                        matrix.postScale(scale, scale, mid[0], mid[1]);
                    }
                    break;
                }
                break;

             //Multiple fingers on screen.
            case MotionEvent.ACTION_POINTER_DOWN:
                //TODO zooming on the image.
                Log.i(Constants.POINTER, "Other fingers");
                //getting spacing between 2 fingers
                oldDist = spacing(motionEvent);
                if (oldDist > 10f) {
                    Log.d("TAG", "mode=ZOOM");
                    savedMatrix.set(matrix);
                    midPoint(mid, motionEvent);
                    mode = Constants.MODE_ZOOM;

                }
                break;
        }
        imgV.setImageMatrix(savedMatrix);
        return true;
    }

    private float spacing(MotionEvent event){
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(float[] md, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        md[0]=(x / 2);
        md[1]=(y / 2);
    }


}
