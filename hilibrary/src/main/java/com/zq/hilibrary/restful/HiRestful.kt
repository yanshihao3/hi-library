package com.zq.hilibrary.restful

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-26 11:05
 **/
class HiRestful(val baseUrl: String, callFactory: HiCall.Factory) {
    private var interceptors: MutableList<HiInterceptor> = mutableListOf()
    private var methodService: ConcurrentHashMap<Method, MethodParser> = ConcurrentHashMap();
    private var scheduler: Scheduler

    init {
        scheduler = Scheduler(callFactory, interceptors)
    }

    fun addInterceptor(interceptor: HiInterceptor) {
        interceptors.add(interceptor)
    }

    /**
     * interface ApiService {
     *  @Headers("auth-token:token", "accountId:123456")
     *  @BaseUrl("https://api.devio.org/as/")
     *  @POST("/cities/{province}")
     *  @GET("/cities")
     * fun listCities(@Path("province") province: Int,@Filed("page") page: Int): HiCall<JsonObject>
     * }
     */
    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf(service)
        ) { _, method, args ->
            var methodParser = methodService[method]
            if (methodParser == null) {
                methodParser = MethodParser.parse(baseUrl, method)
                methodService[method] = methodParser
            }
            //bugFix：此处 应当考虑到 methodParser复用，每次调用都应当解析入参
            val request = methodParser.newRequest(method, args)
            scheduler.newCall(request)
        } as T
    }

}