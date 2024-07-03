package com.hzy.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.WindowManager

/**
 * 手机屏幕工具类
 */
object ScreenUtil {
    /**
     * 获取屏幕的宽度，单位px
     */
    fun getScreenWidth(context: Context): Int {
        return getDisplayMetrics(context).widthPixels
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(activity: Activity): Int {
        return getDefaultDisplay(activity).width
    }

    /**
     * 获取屏幕的高度，单位px
     */
    fun getScreenHeight(context: Context): Int {
        return getDisplayMetrics(context).heightPixels
    }

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(activity: Activity): Int {
        return getDefaultDisplay(activity).height
    }

    /**
     * 获取Display对象
     */
    private fun getDefaultDisplay(activity: Activity): Display {
        return activity.windowManager.defaultDisplay
    }

    /**
     * 获取像素密度，即一个dp单位包含多少个px单位。
     */
    fun getDensity(context: Context): Float {
        return getDisplayMetrics(context).density
    }

    /**
     * 获取DisplayMetrics
     */
    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        // 从系统服务中获取窗口管理器
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        // 从默认显示器中获取显示参数保存到DisplayMetrics
        windowManager.defaultDisplay.getMetrics(outMetrics)
        return outMetrics
    }

    /**
     * 获取view的截图
     */
    fun takeViewScreenshot(view: View): Bitmap {
        // 先开启绘图缓存，开启后才能缓存图片信息
        view.isDrawingCacheEnabled = true
        // 开始构建绘图缓存
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        // 关闭绘图缓存
        view.isDrawingCacheEnabled = false
        view.destroyDrawingCache()
        return bitmap
    }

    /**
     * 获取activity的截图，不包括状态栏
     */
    fun takeScreenshot(activity: Activity): Bitmap {
        val view = activity.window.decorView
        val bitmap = takeViewScreenshot(view)
        return Bitmap.createBitmap(bitmap, 0, getStatusBarHeight(activity), getScreenWidth(activity), getScreenHeight(activity) - getStatusBarHeight(activity))
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(activity: Activity): Int {
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        return frame.top
    }
}