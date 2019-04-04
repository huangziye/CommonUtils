package com.hzy.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent

/**
 * Activity 相关工具类
 * @author: ziye_huang
 * @date: 2019/1/16
 */
object ActivityUtil {

    /**
     * 判断 Activity 是否存在
     */
    fun existActivity(context: Context, intent: Intent) = intent.resolveActivity(context.packageManager) != null

    /**
     * 获取当前 Activity 的名字
     *
     * @param activity 上下文对象
     * @return 包含包名的 Activity 名字
     */
    fun getCurrentActivityName(activity: Activity): String {
        val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var tasks = manager.getRunningTasks(1)
        val task = tasks[0]
        var topActivity = task.topActivity
        return topActivity.className
    }

}