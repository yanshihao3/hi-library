package com.zq.hilibrary.util

import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-29 11:36
 **/
object DataBus {

    private val eventMap = ConcurrentHashMap<String, StickyLiveData<*>>()

    fun <T> with(eventName: String): StickyLiveData<T> {
        //基于事件名称 订阅 分发消息
        // 由于一个livedata 只能发送一种数据类型，所以 不同的event 事件，需要使用不同的livedata实例 分发
        var stickyLiveData = eventMap[eventName]
        if (stickyLiveData == null) {
            stickyLiveData = StickyLiveData<T>(eventName)
            eventMap[eventName] = stickyLiveData
        }
        return stickyLiveData as StickyLiveData<T>
    }


    class StickyLiveData<T>(val eventName: String) : LiveData<T>() {

        var version: Int = 0

        var stickyData: T? = null

        fun setStickData(stickyData: T) {
            this.stickyData = stickyData
            setValue(stickyData)
        }

        fun postStickData(stickyData: T) {
            this.stickyData = stickyData
            postValue(stickyData)
        }

        override fun setValue(value: T) {
            super.setValue(value)
            version++
        }

        override fun postValue(value: T) {
            super.postValue(value)
            version++
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            observeSticky(owner, false, observer)
        }

        fun observeSticky(owner: LifecycleOwner, sticky: Boolean, observer: Observer<in T>) {
            owner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    eventMap.remove(eventName)
                }

            })
            super.observe(owner, StickyObserver(this, sticky, observer))
        }
    }

    class StickyObserver<T>(
        private val stickyLiveData: StickyLiveData<T>,
        private val sticky: Boolean,
        private val observer: Observer<in T>
    ) : Observer<T> {

        private var lastVersion = stickyLiveData.version
        override fun onChanged(t: T) {

            if (lastVersion >= stickyLiveData.version) {
                if (sticky && stickyLiveData.stickyData != null) {
                    observer.onChanged(stickyLiveData.stickyData)
                }
                return
            }
            lastVersion = stickyLiveData.version
            observer.onChanged(t)
        }

    }
}


