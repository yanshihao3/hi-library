package com.zq.hi_library

import android.app.Application
import com.google.gson.Gson
import com.zq.hilibrary.log.ConsolePrinter
import com.zq.hilibrary.log.LogConfig
import com.zq.hilibrary.log.LogManager
import com.zq.hilibrary.log.ViewPrinter

/**
 * @program: hi-library
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-15 16:41
 **/
class MApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LogManager.init(object : LogConfig() {
            override fun injectJsonParser(): JsonParse {
                return object : JsonParse {
                    override fun toJson(src: Any): String {
                        return Gson().toJson(src)
                    }
                }
            }

            override fun getGlobalTag(): String {
                return "demo"
            }

            override fun enable(): Boolean {
                return true
            }
        })
        LogManager.addPrinter(ConsolePrinter())
    }
}