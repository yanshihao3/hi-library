package com.zq.hilibrary.log

/**
 * @program: hi-library
 *
 * @description: 日志格式接口
 *
 * @author: 闫世豪
 *
 * **/
interface LogFormatter<T> {
    fun format(t: T): String
}