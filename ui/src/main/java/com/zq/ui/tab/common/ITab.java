package com.zq.ui.tab.common;

import androidx.annotation.NonNull;

/**
 * @program: hi-library
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-16 12:20
 **/
public interface ITab<D> extends ITabLayout.OnTabSelectedListener {
    void setTabInfo(@NonNull D data);

    /**
     * 动态修改某个item的大小
     */

    void resetHeight(int height);
}
