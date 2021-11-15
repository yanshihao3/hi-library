package com.zq.hilibrary.log

import java.lang.StringBuilder

/**
 * @program: hi-library
 *
 * @description: 堆栈信息格式化类
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-15 17:32
 **/

class StackTraceFormatter : LogFormatter<Array<StackTraceElement>> {

    override fun format(t: Array<StackTraceElement>): String {
        val sb = StringBuilder()
        val size = t.size
        for ((i, value) in t.withIndex()) {
            if (i == 0) {
                sb.append("stackTrace:  \n");
            }
            if (i !=size - 1) {
                sb.append("\t├ ");
                sb.append(value.toString());
                sb.append("\n");
            } else {
                sb.append("\t└ ");
                sb.append(value.toString());
            }
        }

        return sb.toString()
    }
}