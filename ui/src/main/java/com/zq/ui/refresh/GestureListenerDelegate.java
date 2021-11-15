package com.zq.ui.refresh;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @program: hi-library
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-16 17:22
 **/
public class GestureListenerDelegate implements GestureDetector.OnGestureListener {

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
