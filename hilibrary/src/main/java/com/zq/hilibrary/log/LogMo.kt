package com.zq.hilibrary.log

import java.text.SimpleDateFormat
import java.util.*

/**
 * @program: hi-library
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-15 18:56
 **/

class LogMo(var timeMillis: Long, var level: Int, var tag: String, var log: String) {

    fun flattenedLog(): String {
        return """
            $flattened
            $log
            """.trimIndent()
    }

    val flattened: String
        get() = format(timeMillis) + '|' + level + '|' + tag + "|:"

    private fun format(timeMillis: Long): String {
        return sdf.format(timeMillis)
    }

    companion object {
        private val sdf: SimpleDateFormat = SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.CHINA)
    }
}