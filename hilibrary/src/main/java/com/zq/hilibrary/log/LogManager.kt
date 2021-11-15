package com.zq.hilibrary.log

import androidx.annotation.Nullable

/**
 * @program: hi-library
 *
 * @description: log 管理类
 *
 * @author: 闫世豪
 *
 **/
class LogManager private constructor() {

    private lateinit var config: LogConfig

    private lateinit var printers: MutableList<LogPrinter>

    companion object {

        private val instance = SingletonHolder.holder

        @JvmStatic
        fun init(@Nullable config: LogConfig) {
            instance.config = config
            instance.printers = mutableListOf()
        }

        @JvmStatic
        fun getConfig() = instance.config

        @JvmStatic
        fun getPrinter() =
            instance.printers

        @JvmStatic
        fun addPrinter(printer: LogPrinter) {
            instance.printers.add(printer)
        }

        @JvmStatic
        fun removePrinter(printer: LogPrinter) {
            instance.printers.remove(printer)
        }
    }

    private object SingletonHolder {
        val holder = LogManager()
    }
}