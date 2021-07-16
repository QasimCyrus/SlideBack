package com.cyrus.slideback

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import kotlin.math.min

/**
 * 滑动返回视图
 *
 * @author Cyrus 2021/5/25
 */
@SuppressLint("ViewConstructor")
internal class SlideBackView(
    context: Context,
    private val iSlideBackViewProxy: ISlideBackViewProxy
) : View(context), ISlideBackViewProxy {

    /** 从右到左 */
    var rtl: Boolean = false

    private var currentValue: Float = 0f
    private var valueAnimator: ValueAnimator? = null

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override val viewWidth: Int = iSlideBackViewProxy.viewWidth

    override val viewHeight: Int = iSlideBackViewProxy.viewHeight

    override fun onDrawView(canvas: Canvas, slideWidth: Float, rtl: Boolean) {
        iSlideBackViewProxy.onDrawView(canvas, slideWidth, rtl)
    }

    /** 更新视图 */
    fun update(value: Float) {
        val updateValue = min(value, iSlideBackViewProxy.viewWidth.toFloat())
        if (currentValue == updateValue) return

        cancelAnimator()
        currentValue = updateValue
        invalidate()
    }

    /** 重置视图 */
    fun reset() {
        valueAnimator = ValueAnimator.ofFloat(currentValue, 0f).apply {
            duration = 200
            addUpdateListener {
                currentValue = it.animatedValue as Float
                postInvalidate()
            }
            interpolator = DecelerateInterpolator()
        }.also {
            it.start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        onDrawView(canvas, currentValue, rtl)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelAnimator()
    }

    private fun cancelAnimator() {
        valueAnimator?.cancel()
    }
}