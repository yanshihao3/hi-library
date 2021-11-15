package com.zq.hilibrary.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

/**
 * @program: ASProj
 *
 * @description: activity任务栈管理
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-22 11:26
 **/
class ActivityManager private constructor() {

    private val activityRefs = mutableListOf<WeakReference<Activity>>()

    private val frontBackCallback = mutableListOf<FrontBackCallback>()

    private var activityStartCount = 0

    public var front = true


    val topActivity: Activity?
        get() {
            if (activityRefs.isEmpty())
                return null
            if (activityRefs[activityRefs.size - 1].get() != null) {
                return activityRefs[activityRefs.size - 1].get()
            }
            return null
        }

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(AppActivityLifecycleCallbacks())
    }

    inner class AppActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activityRefs.add(WeakReference(activity))
        }

        override fun onActivityStarted(activity: Activity) {
            activityStartCount++
            if (!front && activityStartCount > 0) {
                front = true
                onFrontBackChanged(front)
            }
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {
            activityStartCount--
            if (front && activityStartCount <= 0) {
                front = false
                onFrontBackChanged(front)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            activityRefs.forEach {
                if (it.get() != null && it.get() == activity) {
                    activityRefs.remove(it)
                    return@forEach
                }
            }
        }
    }

    public fun addFrontBackCallback(backCallback: FrontBackCallback) {
        this.frontBackCallback.add(backCallback)
    }

    private fun removeFrontBackCallback(backCallback: FrontBackCallback) {
        this.frontBackCallback.remove(backCallback)
    }

    private fun onFrontBackChanged(front: Boolean) {
        frontBackCallback.forEach {
            it.onChanged(front)
        }
    }

    interface FrontBackCallback {
        /**
         * activity是否处于前台  true 是 处于前台   false 处于后台
         */
        fun onChanged(front: Boolean)
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityManager()
        }
    }
}