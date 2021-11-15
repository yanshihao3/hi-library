package com.zq.ui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.zq.ui.R;
import com.zq.ui.banner.core.BannerDelegate;
import com.zq.ui.banner.core.BannerMo;
import com.zq.ui.banner.core.IBanner;
import com.zq.ui.banner.core.IBindAdapter;
import com.zq.ui.banner.indicator.Indicator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @program: hi-library
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-17 11:47
 **/
public class Banner extends FrameLayout implements IBanner {

    private BannerDelegate mDelegate;

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDelegate = new BannerDelegate(context, this);
        initCustomAttrs(context, attrs);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        boolean autoPlay = typedArray.getBoolean(R.styleable.Banner_autoPlay, true);
        boolean loop = typedArray.getBoolean(R.styleable.Banner_loop, false);
        int intervalTime = typedArray.getInteger(R.styleable.Banner_intervalTime, -1);
        setAutoPlay(autoPlay);
        setLoop(loop);
        setIntervalTime(intervalTime);
        typedArray.recycle();
    }


    @Override
    public void setBannerData(int layoutResId, @NonNull @NotNull List<? extends BannerMo> models) {
        mDelegate.setBannerData(layoutResId, models);
    }

    @Override
    public void setBannerData(@NonNull @NotNull List<? extends BannerMo> models) {
        mDelegate.setBannerData(models);
    }

    @Override
    public void setIndicator(Indicator indicator) {
        mDelegate.setIndicator(indicator);
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        mDelegate.setAutoPlay(autoPlay);
    }

    @Override
    public void setLoop(boolean loop) {
        mDelegate.setLoop(loop);
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        mDelegate.setIntervalTime(intervalTime);
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        mDelegate.setBindAdapter(bindAdapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mDelegate.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        mDelegate.setOnBannerClickListener(onBannerClickListener);
    }

    @Override
    public void setScrollDuration(int duration) {
        mDelegate.setScrollDuration(duration);
    }
}
