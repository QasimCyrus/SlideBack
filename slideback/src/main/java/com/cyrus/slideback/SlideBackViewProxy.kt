package com.cyrus.slideback

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.cyrus.slideback.ext.dpToPx

/**
 * 滑动返回视图绘制代理
 *
 * @author Cyrus 2021/5/25
 */
class SlideBackViewProxy : ISlideBackViewProxy {

    override val viewWidth: Int by lazy { dpToPx(50f) }
    override val viewHeight: Int by lazy { dpToPx(200f) }

    private val bezierPath: Path = Path()
    private val bezierPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private val arrowWidth = dpToPx(4f)
    private val arrowPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = dpToPx(1.5f).toFloat()
        strokeCap = Paint.Cap.ROUND
    }

    override fun onDrawView(canvas: Canvas, slideWidth: Float, rtl: Boolean) {
        val progress = slideWidth / viewWidth
        if (progress <= 0f) return

        val centerY = viewHeight / 2f
        val canvasWidth = canvas.width.toFloat()

        // 贝塞尔曲线
        val bezierWidth = slideWidth / 2f
        bezierPath.reset()
        bezierPath.moveTo(if (rtl) canvasWidth else 0f, 0f)
        bezierPath.cubicTo(
            if (rtl) canvasWidth else 0f,
            viewHeight / 4f,
            if (rtl) canvasWidth - bezierWidth else bezierWidth,
            viewHeight * 3f / 8f,
            if (rtl) canvasWidth - bezierWidth else bezierWidth,
            centerY
        )
        bezierPath.cubicTo(
            if (rtl) canvasWidth - bezierWidth else bezierWidth,
            viewHeight * 5f / 8f,
            if (rtl) canvasWidth else 0f,
            viewHeight * 3f / 4f,
            if (rtl) canvasWidth else 0f,
            viewHeight.toFloat()
        )
        bezierPaint.alpha = (200f * progress).toInt()
        canvas.drawPath(bezierPath, bezierPaint)

        // 箭头
        arrowPaint.alpha = (255f * progress).toInt()
        val arrowLeft = if (rtl) {
            canvasWidth - slideWidth / 4f
        } else {
            slideWidth / 4f - arrowWidth
        }
        canvas.drawLine(
            arrowLeft + arrowWidth,
            centerY - arrowWidth,
            arrowLeft,
            centerY,
            arrowPaint
        )
        canvas.drawLine(
            arrowLeft,
            centerY,
            arrowLeft + arrowWidth,
            centerY + arrowWidth,
            arrowPaint
        )
    }
}