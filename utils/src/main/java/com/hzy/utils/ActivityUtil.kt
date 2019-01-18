package com.hzy.utils

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

}