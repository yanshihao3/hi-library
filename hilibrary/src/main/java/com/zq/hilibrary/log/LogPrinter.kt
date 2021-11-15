package com.zq.hilibrary.log

/**
 * @program: hi-library
 *
 * @description: 日志打印接口
 *
 * @author: 闫世豪
 *
 **/
interface LogPrinter {

    fun print(config: LogConfig, level: Int, tag: String, printString: String)
}