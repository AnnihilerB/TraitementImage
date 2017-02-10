package com.soft.ali.traitementimage;

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
        //Coordinates of the moving finger

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            //One finger on the screen.
            case MotionEvent.ACTION_DOWN:
                Log.i("PTR", "One finger");
                curX = motionEvent.getX();
                curY = motionEvent.getY();
                Log.i("VAL DOWN", String.valueOf(curX) + " "+String.valueOf(curY));
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("PTR", "Move");
                newX = motionEvent.getX();
                newY = motionEvent.getY();
                view.scrollBy((int)(curX-newX), (int)(curY - newY));
                curX = newX;
                curY = newY;
                break;

             //Multiple fingers on screen.
            case MotionEvent.ACTION_POINTER_DOWN:
                //TODO zooming on the image.
                Log.i("PTR", "Other fingers");
                break;
        }
        return true;
    }

}
