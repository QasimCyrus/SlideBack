package com.cyrus.slideback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.cyrus.slideback.ext.dpToPx

/**
 * 滑动返回
 *
 * @author Cyrus 2021/5/24
 */
class SlideBack private constructor(private val builder: Builder) {

    /** 依附到 Application */
    fun attachToApplication(application: Application) {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                attachToActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                // do nothing
            }

            override fun onActivityResumed(activity: Activity) {
                // do nothing
            }

            override fun onActivityPaused(activity: Activity) {
                // do nothing
            }

            override fun onActivityStopped(activity: Activity) {
                // do nothing
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                // do nothing
            }

            override fun onActivityDestroyed(activity: Activity) {
                // do nothing
            }
        })
    }

    /** 依附到 Activity */
    private fun attachToActivity(activity: Activity) {
        SlideBackLayout(
            activity,
            builder.edgeWidth,
            builder.backDeltaX,
            builder.iSlideBackViewProxy
        ).attachToActivity(activity)
    }

    class Builder {

        /** 触发滑动返回流程的边缘宽度范围, 单位：px */
        internal var edgeWidth: Int = dpToPx(18f)

        /** 执行返回操作的 X 轴滑动增量, 单位：px */
        internal var backDeltaX: Int = dpToPx(50f)

        /** 滑动返回视图绘制代理接口 */
        internal var iSlideBackViewProxy: ISlideBackViewProxy? = null

        fun setEdgeWidth(edgeWidth: Int) = apply {
            this.edgeWidth = edgeWidth
        }

        fun setBackDeltaX(backDeltaX: Int) = apply {
            this.backDeltaX = backDeltaX
        }

        fun setSlideBackViewProxy(iSlideBackViewProxy: ISlideBackViewProxy) = apply {
            this.iSlideBackViewProxy = iSlideBackViewProxy
        }

        fun build(): SlideBack = SlideBack(this)
    }
}