package com.zq.ui.refresh;

/**
 * @program: hi-library
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-16 16:56
 **/

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zq.hilibrary.util.DisplayUtil;

/**
 * 下来刷新的视图，可以重载这个类，实现不同的刷新头部
 */
public abstract class OverView extends FrameLayout {

    private RefreshState mState = RefreshState.STATE_INIT;

    /**
     * 触发下拉刷新的最小高度
     */
    public int mPullRefreshHeight;

    /**
     * 最小阻尼
     */
    public float minDamp = 1.6f;

    /**
     * 最大阻尼
     */
    public float maxDamp = 2.2f;


    public OverView(@NonNull Context context) {
        this(context, null);
    }

    public OverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preInit();
    }

    protected void preInit() {
        mPullRefreshHeight = DisplayUtil.dp2px(66, getResources());
        init();
    }


    /**
     * 初始化
     */
    public abstract void init();

    /**
     * y 轴 方向 滚动距离
     *
     * @param scrollY           y 轴 方向 滚动距 离
     * @param pullRefreshHeight 下拉刷新高度
     */
    protected abstract void onScroll(int scrollY, int pullRefreshHeight);


    /**
     * 显示 overlay
     */
    protected abstract void onVisible();

    /**
     * 超过Overlay，释放就会加载
     */
    public abstract void onOver();

    /**
     * 开始刷新
     */
    public abstract void onRefresh();

    /**
     * 刷新完成
     */
    public abstract void onFinish();

    public void setState(RefreshState state) {
        mState = state;
    }

    public RefreshState getState() {
        return mState;
    }

    public enum RefreshState {
        /**
         * 初始态
         */
        STATE_INIT,
        /**
         * header 展示的状态
         */
        STATE_VISIBLE,

        /**
         * 超出可刷新距离的状态
         */
        STATE_OVER,
        /**
         * 刷新中的状态
         */
        STATE_REFRESH,
        /**
         * 超出刷新位置松开手后的状态
         */
        STATE_OVER_RELEASE,

    }
}
