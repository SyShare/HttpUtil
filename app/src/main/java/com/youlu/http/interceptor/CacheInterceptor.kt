package com.youlu.http.interceptor

import com.youlu.http.AppContext
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Author by Administrator , Date on 2018/12/5.
 * PS: Not easy to write code, please indicate.
 */
class CacheInterceptor() : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response
        val request: Request


        if (AppContext.getInstance().isNetWorkValid) {
            //有网络时检查最近的60秒缓存
            request = chain.request().also {
                it.newBuilder()
                        .cacheControl(CacheControl.Builder()
                                .maxAge(60, TimeUnit.SECONDS)
                                .build())
            }
        } else {
            //无网络时可接受的最大时长的一个本地缓存
            request = chain.request().also {
                it.newBuilder()
                        .cacheControl(CacheControl.Builder()
                                .onlyIfCached()
                                .maxStale(1, TimeUnit.DAYS)
                                .build())
            }
        }

        response = chain.proceed(request)
        return response.newBuilder().build()
    }
}