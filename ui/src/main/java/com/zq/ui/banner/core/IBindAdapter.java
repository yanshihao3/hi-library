package com.zq.ui.banner.core;

/**
 * @program: hi-library
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-17 11:30
 **/

/**
 * 基于该接口可以实现数据的绑定和框架层解耦
 */
public interface IBindAdapter {
    void onBind(BannerAdapter.BannerViewHolder viewHolder, BannerMo mo, int position);

}
