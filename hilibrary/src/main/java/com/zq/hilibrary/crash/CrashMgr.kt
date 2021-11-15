package com.zq.hilibrary.crash

import com.zq.hilibrary.util.AppGlobals
import java.io.File

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-12 12:00
 **/
object CrashMgr {
    fun init() {
        val javaCrashDir = getJavaCrashDir()
        val nativeCrashDir = getNativeCrashDir()

        CrashHandler.init(javaCrashDir.absolutePath)
        //NativeCrashHandler.init(nativeCrashDir.absolutePath)
    }

    private fun getJavaCrashDir(): File {
        val javaCrashFile = File(AppGlobals.get()!!.cacheDir, CRASH_DIR_JAVA)
        if (!javaCrashFile.exists()) {
            javaCrashFile.mkdirs()
        }
        return javaCrashFile
    }


    private fun getNativeCrashDir(): File {
        val nativeCrashFile = File(AppGlobals.get()!!.cacheDir, CRASH_DIR_NATIVE)
        if (!nativeCrashFile.exists()) {
            nativeCrashFile.mkdirs()
        }
        return nativeCrashFile
    }

    public fun crashFiles(): Array<File> {
        return getJavaCrashDir().listFiles() + getNativeCrashDir().listFiles()
    }


    private const val CRASH_DIR_JAVA = "java_crash"
    private const val CRASH_DIR_NATIVE = "native_crash"


}