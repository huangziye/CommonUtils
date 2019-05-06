package com.hzy.utils

import android.content.Context
import android.widget.Toast

/**
 * 扩展函数
 * @author: ziye_huang
 * @date: 2019/5/6
 */

/**
 * 字符串以“*”显示
 * @param startIndex 开始下标
 * @param endIndex 结束下标
 */
fun String.starDisplay(startIndex: Int, endIndex: Int): String {
    if (startIndex < 0 || endIndex > this.length - 1) return this
    val sb = StringBuilder()
    for (index in 0 until length) {
        sb.append(if (index in startIndex..endIndex) "*" else this[index])
    }
    return sb.toString()
}

/**
 * toast 信息
 */
fun Any.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this.toString(), duration).apply { show() }
}



 