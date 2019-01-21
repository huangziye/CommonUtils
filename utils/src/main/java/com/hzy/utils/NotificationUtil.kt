package com.hzy.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat

/**
 * Notification 通知栏相关
 * @author: ziye_huang
 * @date: 2019/1/18
 */
object NotificationUtil {

    /**
     *
     * @param bigText 是否是大文本显示
     * @param context 上下文对象
     * @param notificationId 消息id
     * @param channelId 通道id
     * @param channelName 通道名字
     * @param channelDescription 通道描述
     * @param contentTitle 指定通知的标题内容
     * @param contentText 指定通知的正文内容
     * @param bigText 是否是大文本显示
     * @param smallIcon 设置通知的小图标，小图标显示在系统状态栏
     * @param largeIcon 当下拉系统状态栏时，可以看到设置的大图标
     * @param eventTime 指定通知被创建的时间
     * @param pendingIntent 响应点击事件的Intent
     */
    fun createNotification(
        context: Context,
        notificationId: Int,
        channelId: String,
        channelName: String,
        channelDescription: String,
        contentTitle: String,
        contentText: String,
        bigText: Boolean = false,
        @DrawableRes smallIcon: Int,
        @DrawableRes largeIcon: Int,
        eventTime: Long = System.currentTimeMillis(),
        pendingIntent: PendingIntent
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
        //设置文本信息是否是多行显示
        if (bigText) {
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
        }
        builder
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(smallIcon)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, largeIcon))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setWhen(eventTime)
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager, channelId, channelName, channelDescription)
        notificationManager.notify(notificationId, builder.build())
    }

    /**
     * 创建 NotificationChannel
     */
    private fun createNotificationChannel(
        notificationManager: NotificationManager,
        channelId: String,
        channelName: String,
        channelDescription: String
    ) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }
}