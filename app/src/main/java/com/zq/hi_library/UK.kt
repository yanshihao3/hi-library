package com.zq.hi_library

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes

/**
 * @program: hi-library
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-17 18:02
 **/
class UK {
}


fun <T : View> Activity.find(@IdRes id: Int): T {
    return findViewById<T>(id)
}