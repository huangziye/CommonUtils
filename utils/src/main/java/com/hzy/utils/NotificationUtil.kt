package com.hzy.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.SystemClock
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput

/**
 * Notification 通知栏相关
 * @author: ziye_huang
 * @date: 2019/1/18
 */
object NotificationUtil {

    const val RESULT_KEY = "result_key"
    const val NOTIFICATION_ID = 1001
    const val CHANNEL_ID = "channel id"
    const val CHANNEL_NAME = "channel name"
    const val CHANNEL_DESCRIPTION = "channel description"

    /**
     *
     * @param context 上下文对象
     * @param contentTitle 指定通知的标题内容
     * @param contentText 指定通知的正文内容
     * @param smallIcon 设置通知的小图标，小图标显示在系统状态栏
     * @param largeIcon 当下拉系统状态栏时，可以看到设置的大图标
     * @param pendingIntent 响应点击事件的Intent
     */
    fun createNotificationByStyle(
        context: Context,
        contentTitle: String,
        contentText: String,
        @DrawableRes smallIcon: Int,
        @DrawableRes largeIcon: Int,
        style: NotificationCompat.Style? = null,
        pendingIntent: PendingIntent
    ) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        if (null != style) {
            builder.setStyle(style)
        }
        builder
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(smallIcon)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, largeIcon))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setWhen(System.currentTimeMillis())
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(
            notificationManager,
            CHANNEL_ID,
            CHANNEL_NAME,
            CHANNEL_DESCRIPTION
        )
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    /**
     *
     * @param context 上下文对象
     * @param contentTitle 指定通知的标题内容
     * @param contentText 指定通知的正文内容
     * @param bigText 是否是大文本显示
     * @param smallIcon 设置通知的小图标，小图标显示在系统状态栏
     * @param largeIcon 当下拉系统状态栏时，可以看到设置的大图标
     * @param pendingIntent 响应点击事件的Intent
     */
    fun createNotification(
        context: Context,
        contentTitle: String,
        contentText: String,
        bigText: Boolean? = false,
        @DrawableRes smallIcon: Int,
        @DrawableRes largeIcon: Int,
        pendingIntent: PendingIntent
    ) {
        val bigText =
            if (bigText!!) NotificationCompat.BigTextStyle().bigText(contentText) else null
        createNotificationByStyle(
            context,
            contentTitle,
            contentText,
            smallIcon,
            largeIcon,
            bigText,
            pendingIntent
        )
    }

    /**
     *
     * @param context 上下文对象
     * @param contentTitle 指定通知的标题内容
     * @param contentText 指定通知的正文内容
     * @param bigPictureStyle 是否是大图片显示
     * @param smallIcon 设置通知的小图标，小图标显示在系统状态栏
     * @param largeIcon 当下拉系统状态栏时，可以看到设置的大图标
     * @param bigBitmap 大图片
     * @param pendingIntent 响应点击事件的Intent
     */
    fun createNotificationWithBigPicture(
        context: Context,
        contentTitle: String,
        contentText: String,
        bigPictureStyle: Boolean? = false,
        @DrawableRes smallIcon: Int,
        @DrawableRes largeIcon: Int,
        bigBitmap: Bitmap,
        pendingIntent: PendingIntent
    ) {
        val bigPictureStyle =
            if (bigPictureStyle!!) NotificationCompat.BigPictureStyle()
                .bigPicture(bigBitmap) else null
        createNotificationByStyle(
            context,
            contentTitle,
            contentText,
            smallIcon,
            largeIcon,
            bigPictureStyle,
            pendingIntent
        )
    }

    /**
     * 创建通知直接回复
     */
    fun createNotificationWithRemoteInput(
        context: Context,
        contentTitle: String,
        contentText: String,
        replyLabel: String,
        @DrawableRes smallIcon: Int,
        @DrawableRes replyIcon: Int,
        replyPendingIntent: PendingIntent
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val remoteInput = createReplyRemoteInput(RESULT_KEY, replyLabel)
        val action = createReplyAction(replyIcon, replyLabel, replyPendingIntent, remoteInput)
        // Build the notification and add the action.
        val newMessageNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIcon)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .addAction(action)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .build()
        createNotificationChannel(
            notificationManager,
            CHANNEL_ID,
            CHANNEL_NAME,
            CHANNEL_DESCRIPTION
        )
        notificationManager.notify(NOTIFICATION_ID, newMessageNotification)
    }

    /**
     * 仅供学习使用，要根据实际需求修改
     */
    fun createNotificationWithProgress(
        context: Context,
        contentTitle: String,
        contentText: String,
        completeText: String,
        @DrawableRes smallIcon: Int,
        max: Int,
        intent: Intent
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Build the notification and add the action.
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIcon)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
        Thread(Runnable {
            for (i in 0..max) {
                if (i == max) {
                    builder.setContentText(completeText)
                    createNotificationChannel(
                        notificationManager,
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        CHANNEL_DESCRIPTION
                    )
                    notificationManager.notify(NOTIFICATION_ID, builder.build())
                    notificationManager.cancel(NOTIFICATION_ID)
                    context.startActivity(intent)
                    break
                }
                SystemClock.sleep(100)
                builder.setProgress(100, i, false)
                createNotificationChannel(
                    notificationManager,
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    CHANNEL_DESCRIPTION
                )
                notificationManager.notify(NOTIFICATION_ID, builder.build())
            }
        }).start()
    }

    /**
     * 自定义 Notification 视图
     */
    fun createNotificationWithCustomView(
        context: Context, @DrawableRes smallIcon: Int,
        customView: RemoteViews,
        pendingIntent: PendingIntent
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIcon)
            .setCustomContentView(customView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentIntent(pendingIntent)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setAutoCancel(true)
        createNotificationChannel(
            notificationManager,
            CHANNEL_ID,
            CHANNEL_NAME,
            CHANNEL_DESCRIPTION
        )
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    /**
     *  创建 RemoteInput
     */
    private fun createReplyRemoteInput(resultKey: String, replyLabel: String): RemoteInput {
        return RemoteInput.Builder(resultKey).run {
            setLabel(replyLabel)
            build()
        }
    }

    /**
     * 创建 NotificationCompat.Action
     */
    private fun createReplyAction(
        @DrawableRes replyIcon: Int,
        replyLabel: String,
        replyPendingIntent: PendingIntent,
        remoteInput: RemoteInput
    ): NotificationCompat.Action {
        // Create the reply action and add the remote input.
        return NotificationCompat.Action.Builder(replyIcon, replyLabel, replyPendingIntent)
            .addRemoteInput(remoteInput)
            .build()
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