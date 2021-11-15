package com.zq.ui.banner.core;

import android.content.Context;
import android.widget.Scroller;

/**
 * @program: hi-library
 * @description: 用于设置滚动的时长
 * @author: 闫世豪
 * @create: 2021-09-16 19:08
 **/
public class BannerScroller extends Scroller {
    /**
     * 值越大，滑动越慢
     */
    private int mDuration = 1000;

    public BannerScroller(Context context, int duration) {
        super(context);
        mDuration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy,mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
