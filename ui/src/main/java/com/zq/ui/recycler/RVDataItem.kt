package com.zq.ui.recycler

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-22 15:57
 **/
abstract class RVDataItem<DATA, VH : RecyclerView.ViewHolder>(data: DATA? = null) {

    var adapter: RVAdapter? = null
    private var data: DATA? = null

    init {
        this.data = data
    }

    abstract fun onBindData(holder: VH, position: Int)

    /**
     * 绑定数据
     */
    open fun getItemLayoutRes(): Int {
        return -1
    }

    /**
     *返回该item的视图view
     */

    open fun getItemView(parent: ViewGroup): View? {
        return null
    }


    /**
     * 刷新列表
     */
    fun refreshItem() {
        if (adapter != null) adapter!!.refreshItem(this)
    }

    /**
     * 从列表上移除
     */
    fun removeItem() {
        if (adapter != null) adapter!!.removeItem(this)

    }

    /**
     * 该item在列表上占几列,代表的宽度是沾满屏幕
     */
    open fun getSpanSize(): Int {
        return 0
    }

    /**
     * 该item被滑进屏幕
     */
    open fun onViewAttachedToWindow(holder: VH) {

    }

    /**
     * 该item被滑出屏幕
     */
    open fun onViewDetachedFromWindow(holder: VH) {

    }
}