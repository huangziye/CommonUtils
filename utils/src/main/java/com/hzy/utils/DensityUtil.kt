package com.hzy.utils

import android.content.Context

/**
 * 密度相关工具类
 * @author: ziye_huang
 * @date: 2019/1/16
 */
object DensityUtil {

    /**
     * 转换dip为px
     */
    fun dp2px(context: Context, dip: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dip * scale + 0.5f).toInt()
    }

    /**
     * 转换px为dip
     */
    fun px2dp(context: Context, px: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    /**
     * 转换sp为px
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 转换px为sp
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

}