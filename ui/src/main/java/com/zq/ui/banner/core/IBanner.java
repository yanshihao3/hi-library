package com.zq.ui.banner.core;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.zq.ui.banner.indicator.Indicator;

import java.util.List;

/**
 * @program: hi-library
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-17 11:26
 **/
public interface IBanner {

    void setBannerData(@LayoutRes int layoutResId, @NonNull List<? extends BannerMo> models);

    void setBannerData(@NonNull List<? extends BannerMo> models);

    void setIndicator(Indicator hiIndicator);

    void setAutoPlay(boolean autoPlay);

    void setLoop(boolean loop);

    void setIntervalTime(int intervalTime);

    void setBindAdapter(IBindAdapter bindAdapter);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener);

    void setOnBannerClickListener(IBanner.OnBannerClickListener onBannerClickListener);

    void setScrollDuration(int duration);

    interface OnBannerClickListener {
        void onBannerClick(@NonNull BannerAdapter.BannerViewHolder viewHolder, @NonNull BannerMo bannerMo, int position);
    }


}
