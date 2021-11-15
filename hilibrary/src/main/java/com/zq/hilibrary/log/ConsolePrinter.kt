package com.zq.hilibrary.log

import android.util.Log

/**
 * @program: hi-library
 *
 * @description: 控制台 打印器
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-15 17:48
 **/
class ConsolePrinter : LogPrinter {
    override fun print(config: LogConfig, level: Int, tag: String, printString: String) {
        val len = printString.length
        val countOfSub = len / LogConfig.MAX_LEN
        if (countOfSub > 0) {
            var index = 0
            for (i in 0 until countOfSub) {
                Log.println(level, tag, printString.substring(index, index + LogConfig.MAX_LEN))
                index += LogConfig.MAX_LEN
            }
            if (index != len) {
                Log.println(level, tag, printString.substring(index, len))
            }
        }else{
            Log.println(level, tag, printString)
        }
    }
}