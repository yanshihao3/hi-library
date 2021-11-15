package com.zq.ui.refresh;

/**
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-16 16:52
 **/
public interface IRefresh {

    /**
     * 刷新时 是否禁止滚动
     */
    void setDisableRefreshScroll(boolean disableRefreshScroll);

    /**
     * 刷新完成
     */
    void refreshFinished();

    /**
     * 设置刷新的监听器
     *
     * @param listener
     */
    void setRefreshListener(RefreshListener listener);

    /**
     * 设置下拉刷新的视图
     *
     * @param overView
     */
    void setRefreshOverView(OverView overView);

    interface RefreshListener {

        void onRefresh();

        boolean enableRefresh();
    }
}
