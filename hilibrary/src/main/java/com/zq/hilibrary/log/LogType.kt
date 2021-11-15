package com.zq.hilibrary.log

import android.util.Log
import androidx.annotation.IntDef

/**
 * @program: hi-library
 *
 * @description:日志级别定义
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-15 16:04
 **/
public class LogType {

    @IntDef(V, W, D, I, E, A)
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
    @Retention(value = AnnotationRetention.SOURCE)
    annotation class TYPE()


    companion object {
        const val V: Int = Log.VERBOSE
        const val W: Int = Log.WARN
        const val D: Int = Log.DEBUG
        const val I: Int = Log.INFO
        const val E: Int = Log.ERROR
        const val A: Int = Log.ASSERT
    }
}