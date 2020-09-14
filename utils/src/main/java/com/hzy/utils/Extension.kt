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

/**
 * 用自定义的分隔符拼接数据
 * @param separator 自定义分隔符
 * @param prefix 前缀
 * @param postfix 后缀
 */
fun <T> Collection<T>.joinToString(
    separator: String = ",",
    prefix: String = "",
    postfix: String = ""
): String {
    val sb = java.lang.StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) sb.append(separator)
        sb.append(element)
    }
    sb.append(postfix)
    return sb.toString()
}



 