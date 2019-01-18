package com.hzy.utils

import android.content.Context
import android.util.TypedValue

/**
 * 密度相关工具类
 * @author: ziye_huang
 * @date: 2019/1/16
 */
object DensityUtil {

    /**
     * dp to px
     */
    fun dp2px(context: Context, dp: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()

    /**
     * px to dp
     */
    fun px2dp(context: Context, px: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.resources.displayMetrics).toInt()

    /**
     * dp to sp
     */
    fun dp2sp(context: Context, dp: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()

    /**
     * sp to dp
     */
    fun sp2dp(context: Context, sp: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics).toInt()

}