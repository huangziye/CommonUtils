package com.hzy.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

/**
 * Bitmap 相关工具类
 * @author: ziye_huang
 * @date: 2019/1/22
 */
object BitmapUtil {

    /**
     * 获取文件的Base64字符串
     */
    fun getBase64StringByPath(path: String): String? {
        try {
            val file = File(path)
            if (TextUtils.isEmpty(path) || !file.exists()) return null
            val sb = StringBuilder()
            val buffer = ByteArray(1024) { 0 }
            val fis = FileInputStream(file)
            var len = fis.read(buffer)
            while (len != -1) {
                sb.append(Base64.encodeToString(buffer, 0, len, Base64.DEFAULT))
                len = fis.read(buffer)
            }
            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 把bitmap转换成base64
     *
     * @param bitmap
     * @param bitmapQuality
     * @return
     */
    fun getBase64StringFromBitmap(bitmap: Bitmap, bitmapQuality: Int): String {
        val bStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream)
        val bytes = bStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    /**
     * 把base64转换成bitmap
     */
    fun getBitmapFromBase64String(base64String: String): Bitmap? {
        return try {
            val bitmapArray = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}