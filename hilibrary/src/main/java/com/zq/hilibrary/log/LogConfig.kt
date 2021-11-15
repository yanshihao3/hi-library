package com.zq.hilibrary.log

/**
 * @program: hi-library
 *
 * @description:
 *
 * @author: 闫世豪
 *
 **/
abstract class LogConfig {

    open fun getGlobalTag(): String = "hiLog"

    open fun enable() = true

    open fun includeTread(): Boolean {
        return true
    }
    //最大深度
    open fun stackTraceDepth() = 5

    open fun injectJsonParser(): JsonParse? = null

    fun printers(): MutableList<LogPrinter> {
        return mutableListOf()
    }

    companion object {
        //最大长度
        const val MAX_LEN: Int = 512

        val THREAD_FORMATTER by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ThreadFormatter()
        }

        val STACK_TRACE_FORMATTER by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            StackTraceFormatter()
        }
    }


    interface JsonParse {
        fun toJson(src: Any): String
    }
}