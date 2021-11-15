package com.zq.ui.banner.core;

/**
 * @program: hi-library
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-17 11:48
 **/

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.zq.ui.R;
import com.zq.ui.banner.Banner;
import com.zq.ui.banner.indicator.CircleIndicator;
import com.zq.ui.banner.indicator.Indicator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * HiBanner的控制器
 * 辅助HiBanner完成各种功能的控制
 * 将HiBanner的一些逻辑内聚在这，保证暴露给使用者的HiBanner干净整洁
 */
public class BannerDelegate implements IBanner, ViewPager.OnPageChangeListener {

    private Context mContext;

    private Banner mBanner;
    private BannerAdapter mAdapter;
    private Indicator mIndicator;
    private boolean mAutoPlay;
    private boolean mLoop;
    private List<? extends BannerMo> mBannerMos;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int mIntervalTime = 5000;
    private Banner.OnBannerClickListener mOnBannerClickListener;
    private BannerViewPager mViewPager;
    private int mScrollDuration = -1;

    //当前的位置
    private int mPosition = 0;

    public BannerDelegate(Context context, Banner banner) {
        mContext = context;
        mBanner = banner;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (null != mOnPageChangeListener && mAdapter.getRealCount() != 0) {
            mOnPageChangeListener.onPageScrolled(position % mAdapter.getRealCount(), positionOffset, positionOffsetPixels);
        }

    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter.getRealCount() == 0) {
            return;
        }
        position = position % mAdapter.getRealCount();
        this.mPosition = position;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
        if (mIndicator != null) {
            mIndicator.onPointChange(position, mAdapter.getRealCount());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void setBannerData(int layoutResId, @NonNull @NotNull List<? extends BannerMo> models) {
        this.mBannerMos = models;
        init(layoutResId);
    }

    @Override
    public void setBannerData(@NonNull @NotNull List<? extends BannerMo> models) {
        setBannerData(R.layout.banner_item_image, models);
    }

    @Override
    public void setIndicator(Indicator indicator) {
        this.mIndicator = indicator;
        // 可以重新设置 Indicator,重新加载 指示器
        if (mBanner.getChildCount() > 1) {
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

            mIndicator.onInflate(mBannerMos.size());
            //清除缓存view
            mBanner.removeViewAt(1);
            mBanner.addView(mIndicator.get(), layoutParams);
            mIndicator.onPointChange(mPosition, mBannerMos.size());
        }

    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
        if (mAdapter != null) mAdapter.setAutoPlay(mAutoPlay);
        if (mViewPager != null) mViewPager.setAutoPlay(mAutoPlay);
    }

    @Override
    public void setLoop(boolean loop) {
        this.mLoop = loop;
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) {
            this.mIntervalTime = intervalTime;
        }
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        mAdapter.setBindAdapter(bindAdapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.mOnBannerClickListener = onBannerClickListener;
    }

    @Override
    public void setScrollDuration(int duration) {
        this.mScrollDuration = duration;
        if (mViewPager != null && duration > 0) mViewPager.setScrollDuration(duration);
    }

    private void init(@LayoutRes int layoutResId) {
        if (mAdapter == null) {
            mAdapter = new BannerAdapter(mContext);
        }
        if (mIndicator == null) {
            mIndicator = new CircleIndicator(mContext);
        }

        mIndicator.onInflate(mBannerMos.size());

        mAdapter.setLayoutResId(layoutResId);
        mAdapter.setBannerData(mBannerMos);
        mAdapter.setAutoPlay(mAutoPlay);
        mAdapter.setLoop(mLoop);
        mAdapter.setOnBannerClickListener(mOnBannerClickListener);

        mViewPager = new BannerViewPager(mContext);
        mViewPager.setIntervalTime(mIntervalTime);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAutoPlay(mAutoPlay);
        if (mScrollDuration > 0) mViewPager.setScrollDuration(mScrollDuration);
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mViewPager.setAdapter(mAdapter);

        if ((mLoop || mAutoPlay) && mAdapter.getRealCount() != 0) {
            //无限轮播关键点：使第一张能反向滑动到最后一张，已达到无限滚动的效果
            // int firstItem = mAdapter.getFirstItem();
            mViewPager.setCurrentItem(0, false);
        }

        //清除缓存view
        mBanner.removeAllViews();
        mBanner.addView(mViewPager, layoutParams);
        mBanner.addView(mIndicator.get(), layoutParams);
    }
}
