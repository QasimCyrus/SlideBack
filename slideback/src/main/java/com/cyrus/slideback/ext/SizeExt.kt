package com.cyrus.slideback.ext

import android.content.res.Resources
import kotlin.math.ceil

/**
 * 尺寸相关
 *
 * @author Cyrus 2021/5/21
 */
/** dp 转 px */
internal fun dpToPx(dp: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return ceil(dp * scale).toInt()
}