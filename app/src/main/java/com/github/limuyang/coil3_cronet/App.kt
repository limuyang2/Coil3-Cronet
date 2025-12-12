package com.github.limuyang.coil3_cronet

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.CachePolicy
import coil3.request.crossfade
import io.github.limuyang2.coil3.cronet.CronetNetworkFetcherFactory
import org.chromium.net.CronetEngine

/**
 * @author 李沐阳
 * @date 2025/1/18
 * @description
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this

        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.DISABLED)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .components {
                    add(CronetNetworkFetcherFactory(cronetEngine))
                }
                .build()
        }

    }


    companion object {
        lateinit var application: Application

        val cronetEngine: CronetEngine by lazy {
            CronetEngine.Builder(application)
                .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_DISABLED, 1048576)
                .enableHttp2(true)
                .enableQuic(true)
                .enableBrotli(true)
                .build()
        }
    }
}