package com.zq.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @program: hi-library
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-16 17:07
 **/
public class RefreshLayout extends FrameLayout implements IRefresh {

    private static final String TAG = "RefreshLayout";
    private OverView.RefreshState mState;

    private GestureDetector mGestureDetector;//手势

    private IRefresh.RefreshListener mRefreshListener;

    private OverView mOverView;

    private AutoScroller mAutoScroller;

    /**
     * 下拉时，移动的距离
     */
    private int mLastY;
    /**
     * 下拉刷新时 是否可以滚动
     */
    private boolean disableRefreshScroll = true;

    public RefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }


    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), mGestureListener);
        mAutoScroller = new AutoScroller();
    }

    GestureDetector.OnGestureListener mGestureListener = new GestureListenerDelegate() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY) || mRefreshListener != null && !mRefreshListener.enableRefresh()) {
                //横向滑动，或刷新被禁止则不处理
                return false;
            }
            if (disableRefreshScroll && mState == OverView.RefreshState.STATE_REFRESH) {//刷新时是否禁止滑动
                return true;
            }
            View head = getChildAt(0);
            View child = ScrollUtil.findScrollableChild(RefreshLayout.this);
            if (ScrollUtil.childScrolled(child)) {
                //如果列表发生了滚动则不处理
                return false;
            }
            //没有刷新或没有达到可以刷新的距离，且头部已经划出或下拉
            if ((mState != OverView.RefreshState.STATE_REFRESH || head.getBottom() <= mOverView.mPullRefreshHeight) && (head.getBottom() > 0 || distanceY <= 0.0F)) {
                //还在滑动中
                if (mState != OverView.RefreshState.STATE_OVER_RELEASE) {
                    int speed;
                    //阻尼计算
                    if (child.getTop() < mOverView.mPullRefreshHeight) {
                        speed = (int) (mLastY / mOverView.minDamp);
                    } else {
                        speed = (int) (mLastY / mOverView.maxDamp);
                    }
                    //如果是正在刷新状态，则不允许在滑动的时候改变状态
                    boolean bool = moveDown(speed, true);
                    mLastY = (int) (-distanceY);
                    return bool;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    /**
     * 根据偏移量移动header与child
     *
     * @param offsetY 偏移量
     * @param nonAuto 是否非自动滚动触发
     * @return
     */
    private boolean moveDown(int offsetY, boolean nonAuto) {
        View head = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;

        if (childTop <= 0) {//异常情况的补充
            offsetY = -child.getTop();
            //移动head与child的位置，到原始位置
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (mState != OverView.RefreshState.STATE_REFRESH) {
                mState = OverView.RefreshState.STATE_INIT;
            }
        } else if (mState == OverView.RefreshState.STATE_REFRESH && childTop > mOverView.mPullRefreshHeight) {
            //如果正在下拉刷新中，禁止继续下拉
            return false;
        } else if (childTop <= mOverView.mPullRefreshHeight) {//还没超出设定的刷新距离
            if (mOverView.getState() != OverView.RefreshState.STATE_VISIBLE && nonAuto) {//头部开始显示
                mOverView.onVisible();
                mOverView.setState(OverView.RefreshState.STATE_VISIBLE);
                mState = OverView.RefreshState.STATE_VISIBLE;
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (childTop == mOverView.mPullRefreshHeight && mState == OverView.RefreshState.STATE_OVER_RELEASE) {
                refresh();
            }
        } else {
            if (mOverView.getState() != OverView.RefreshState.STATE_OVER && nonAuto) {
                //超出刷新位置
                mOverView.onOver();
                mOverView.setState(OverView.RefreshState.STATE_OVER);
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
        }
        if (mOverView != null) {
            mOverView.onScroll(head.getBottom(), mOverView.mPullRefreshHeight);
        }
        return true;
    }


    /**
     * 刷新
     */
    private void refresh() {
        if (mRefreshListener != null) {
            mState = OverView.RefreshState.STATE_REFRESH;
            mOverView.onRefresh();
            mOverView.setState(OverView.RefreshState.STATE_REFRESH);
            mRefreshListener.onRefresh();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //定义head和child的排列位置
        View head = getChildAt(0);
        View child = getChildAt(1);
        if (head != null && child != null) {
            int childTop = child.getTop();
            if (mState == OverView.RefreshState.STATE_REFRESH) {
                head.layout(0, mOverView.mPullRefreshHeight - head.getMeasuredHeight(), right, mOverView.mPullRefreshHeight);
                child.layout(0, mOverView.mPullRefreshHeight, right, mOverView.mPullRefreshHeight + child.getMeasuredHeight());
            } else {
                //left,top,right,bottom
                head.layout(0, childTop - head.getMeasuredHeight(), right, childTop);
                child.layout(0, childTop, right, childTop + child.getMeasuredHeight());
            }

            View other;
            for (int i = 2; i < getChildCount(); ++i) {
                other = getChildAt(i);
                other.layout(0, top, right, bottom);
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View head = getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL
                || ev.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK) {//松开手
            if (head.getBottom() > 0) {
                if (mState != OverView.RefreshState.STATE_REFRESH) {//非正在刷新  把头部滚动回去
                    recover(head.getBottom());
                    return false;
                }
            }
            mLastY = 0;
        }
        boolean consumed = mGestureDetector.onTouchEvent(ev);
        if ((consumed || (mState != OverView.RefreshState.STATE_INIT && mState != OverView.RefreshState.STATE_REFRESH)) && head.getBottom() != 0) {

            ev.setAction(MotionEvent.ACTION_CANCEL);//让父类接受不到真实的事件
            return super.dispatchTouchEvent(ev);
        }
        if (consumed) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    /**
     * 把头部滚动回去
     *
     * @param dis
     */
    private void recover(int dis) {
        if (mRefreshListener != null && dis > mOverView.mPullRefreshHeight) {
            mAutoScroller.recover(dis - mOverView.mPullRefreshHeight);
            mState = OverView.RefreshState.STATE_OVER_RELEASE;
        } else {
            mAutoScroller.recover(dis);
        }
    }

    @Override
    public void setDisableRefreshScroll(boolean disableRefreshScroll) {
        this.disableRefreshScroll = disableRefreshScroll;
    }

    @Override
    public void refreshFinished() {
        final View head = getChildAt(0);
        mOverView.onFinish();
        mOverView.setState(OverView.RefreshState.STATE_INIT);
        final int bottom = head.getBottom();
        if (bottom > 0) {
            //下over pull 200，height 100
            //  bottom  =100 ,height 100
            recover(bottom);
        }
        mState = OverView.RefreshState.STATE_INIT;
    }

    @Override
    public void setRefreshListener(RefreshListener listener) {
        mRefreshListener = listener;
    }

    /**
     * 设置下拉刷新的视图
     *
     * @param hiOverView
     */
    @Override
    public void setRefreshOverView(OverView hiOverView) {
        if (this.mOverView != null) {
            removeView(mOverView);
        }
        this.mOverView = hiOverView;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mOverView, 0, params);
    }

    /**
     * 借助Scroller实现视图的自动滚动
     * https://juejin.im/post/5c7f4f0351882562ed516ab6
     */
    private class AutoScroller implements Runnable {
        private Scroller mScroller;
        private int mLastY;
        private boolean mIsFinished;

        public AutoScroller() {
            mScroller = new Scroller(getContext(), new LinearInterpolator());
            mIsFinished = true;
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {//还未滚动完成
                moveDown(mLastY - mScroller.getCurrY(), false);
                mLastY = mScroller.getCurrY();
                post(this);
            } else {
                removeCallbacks(this);
                mIsFinished = true;
            }
        }

        void recover(int dis) {
            if (dis <= 0) {
                return;
            }
            removeCallbacks(this);
            mLastY = 0;
            mIsFinished = false;
            mScroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        boolean isFinished() {
            return mIsFinished;
        }

    }
}
