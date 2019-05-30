package com.hzy.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Base64转换工具类
 * @author: ziye_huang
 * @date: 2019/5/30
 */
object Base64Util {
    /**
     * 通过路径生成Base64文件
     *
     * @param path
     * @return
     */
    fun getBase64FromPath(path: String): String {
        var base64 = ""
        try {
            val file = File(path)
            val buffer = ByteArray(file.length().toInt() + 100)
            val length = FileInputStream(file).read(buffer)
            base64 = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return base64
    }

    /**
     * 把bitmap转换成base64
     *
     * @param bitmap
     * @param bitmapQuality
     * @return
     */
    fun getBase64FromBitmap(bitmap: Bitmap, bitmapQuality: Int): String {
        val bStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream)
        val bytes = bStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    /**
     * 把base64转换成bitmap
     *
     * @param string
     * @return
     */
    fun getBitmapFromBase64(string: String): Bitmap {
        var bitmapArray: ByteArray? = null
        try {
            bitmapArray = Base64.decode(string, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray!!.size)
    }
}