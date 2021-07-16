package com.cyrus.slideback

import android.app.Application

/**
 * 应用入口
 *
 * @author Cyrus 2021/7/7
 */
@Suppress("unused")
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SlideBack.Builder()
            .setEdgeWidth(50)
            .setBackDeltaX(100)
            .setSlideBackViewProxy(SlideBackViewProxy())
            .build()
            .attachToApplication(this)
    }
}