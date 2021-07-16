package com.cyrus.slideback

import android.graphics.Canvas

/**
 * 滑动返回视图绘制代理接口，负责绘制逻辑的处理
 *
 * @author Cyrus 2021/5/25
 */
interface ISlideBackViewProxy {

    /** 视图宽度 */
    val viewWidth: Int

    /** 视图高度 */
    val viewHeight: Int

    /** 绘制视图，[slideWidth]是滑动距离，[rtl]指明是否为从右往左 */
    fun onDrawView(canvas: Canvas, slideWidth: Float, rtl: Boolean)
}