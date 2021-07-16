package com.cyrus.slideback

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import java.lang.ref.WeakReference

/**
 * 滑动返回布局，负责手势控制
 *
 * @author Cyrus 2021/5/21
 */
@SuppressLint("ViewConstructor")
internal class SlideBackLayout(
    context: Context,
    /** 触发滑动返回流程的边缘宽度范围, 单位：px */
    private val edgeWidth: Int,
    /** 执行返回操作的 X 轴滑动增量, 单位：px */
    private val backDeltaX: Int,
    /** 滑动返回视图绘制代理接口 */
    iSlideBackViewProxy: ISlideBackViewProxy?
) : FrameLayout(context) {

    /** 记录点击时的 X 轴位置 */
    private var downX: Float = 0f

    /** 记录 X 轴滑动增量 */
    private var deltaX: Float = 0f

    /** 是否正在执行滑动返回流程 */
    private var sliding: Boolean = false

    /** 滑动返回视图 */
    private var slideBackView: SlideBackView? = null

    /** Activity 弱引用 */
    private var activityReference: WeakReference<Activity>? = null

    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        iSlideBackViewProxy?.let {
            slideBackView = SlideBackView(context, it)
            addView(slideBackView)
        }
    }

    /** 依附到 Activity */
    fun attachToActivity(activity: Activity) {
        val viewParent = parent
        if (viewParent is ViewGroup) viewParent.removeView(this)

        val decorView = activity.window.decorView as ViewGroup
        decorView.addView(this)

        activityReference?.clear()
        activityReference = WeakReference(activity)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        sliding = ev.action == MotionEvent.ACTION_DOWN && isInTheEdge(ev.rawX)
        return sliding || super.onInterceptTouchEvent(ev)
    }

    /** 是否应该触发滑动返回流程 */
    private fun isInTheEdge(rawX: Float): Boolean {
        // 处于 左边触发范围 或者 右边触发范围
        return isInLeftEdge(rawX) || isInRightEdge(rawX)
    }

    /** 是否处于左边触发范围 */
    private fun isInLeftEdge(rawX: Float): Boolean {
        return rawX <= edgeWidth
    }

    /** 是否处于右边触发范围 */
    private fun isInRightEdge(rawX: Float): Boolean {
        return rawX >= width - edgeWidth
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!sliding) return super.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.rawX
                deltaX = 0f
                slideBackView?.apply {
                    // 在触发范围足够大（左右触发范围重叠）时，优先触发左侧滑动
                    rtl = !isInLeftEdge(downX)
                    update(0f)
                    scrollTo(0, -(event.rawY - viewHeight / 2).toInt())
                }
            }
            MotionEvent.ACTION_MOVE -> {
                // 在触发范围足够大（左右触发范围重叠）时，优先触发左侧滑动
                deltaX = if (isInLeftEdge(downX)) {
                    event.rawX - downX
                } else {
                    downX - event.rawX
                }
                slideBackView?.update(deltaX)
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_OUTSIDE -> {
                slideBackView?.reset()
                if (deltaX >= backDeltaX) {
                    activityReference?.get()?.onBackPressed()
                }
            }
        }
        return true
    }
}