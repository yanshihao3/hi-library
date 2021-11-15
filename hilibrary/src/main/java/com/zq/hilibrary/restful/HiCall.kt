package com.zq.hilibrary.restful

import java.io.IOException

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-26 10:59
 **/
interface HiCall<T> {

    @Throws(IOException::class)
    fun execute(): HiResponse<T>

    fun enqueue(callback: HiCallback<T>)

    interface Factory {
        fun newCall(request: HiRequest): HiCall<*>
    }
}