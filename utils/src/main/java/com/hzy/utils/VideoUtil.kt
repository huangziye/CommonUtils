package com.hzy.utils

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import java.util.*

/**
 * 视频相关工具类
 * Created by ziye_huang on 2019/1/10.
 */
object VideoUtil {

    /**
     * 获取本地视频某一时间的一帧
     * @param localPath 本地视频的path
     * @param milliscond 要获取某一帧的时间：毫秒值
     *
     * 需要 android.permission.WRITE_EXTERNAL_STORAGE 权限
     */
    fun getLocalVideoBitmap(localPath: String, milliscond: Long): Bitmap? {
        if (TextUtils.isEmpty(localPath)) return null
        val retriever = MediaMetadataRetriever()
        return try {
            //根据文件路径获取缩略图
            retriever.setDataSource(localPath)
            //获取某时间的一帧图片
            retriever.getFrameAtTime(milliscond * 1000, MediaMetadataRetriever.OPTION_CLOSEST)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }

    /**
     * 通过url获取视频的某一帧
     * Android 原生给我们提供了一个MediaMetadataRetriever类，提供了获取url视频某一帧的方法，返回Bitmap
     * @param videoUrl 网络视频的url
     * @param milliscond 要获取某一帧的时间：毫秒值
     *
     * 需要 android.permission.INTERNET、android.permission.WRITE_EXTERNAL_STORAGE 权限
     */
    fun getNetVideoBitmap(videoUrl: String, milliscond: Long): Bitmap? {
        if (TextUtils.isEmpty(videoUrl)) return null
        val retriever = MediaMetadataRetriever()
        return try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, HashMap())
            //获取某时间的一帧图片
            retriever.getFrameAtTime(milliscond * 1000, MediaMetadataRetriever.OPTION_CLOSEST)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }

    /**
     * 形式返回视频文件的播放长度（毫秒值）
     * @param localPath 视频的url地址
     *
     * 需要 android.permission.WRITE_EXTERNAL_STORAGE 权限
     */
    fun getLocalVideoFileLength(localPath: String): Long {
        if (TextUtils.isEmpty(localPath)) return 0
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(localPath)
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
        } catch (e: Exception) {
            e.printStackTrace()
            0
        } finally {
            retriever.release()
        }
    }

    /**
     * 形式返回视频文件的播放长度（毫秒值）
     * @param videoUrl 视频的url地址
     *
     * 需要 android.permission.INTERNET、android.permission.WRITE_EXTERNAL_STORAGE 权限
     */
    fun getNetVideoFileLength(videoUrl: String): Long {
        if (TextUtils.isEmpty(videoUrl)) return 0
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(videoUrl, HashMap())
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
        } catch (e: Exception) {
            e.printStackTrace()
            0
        } finally {
            retriever.release()
        }
    }
}