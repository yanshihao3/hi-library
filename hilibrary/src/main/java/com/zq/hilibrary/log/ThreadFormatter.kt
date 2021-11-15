package com.zq.hilibrary.log

/**
 * @program: hi-library
 *
 * @description: 线程格式化
 *
 * @author: 闫世豪
 *
 **/
class ThreadFormatter : LogFormatter<Thread> {
    override fun format(t: Thread): String {
        return "Thread:${t.name}"
    }
}