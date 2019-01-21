package com.hzy.commonutils

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hzy.utils.NotificationUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv.setOnClickListener {
            sendNotification()
//            sendRemoteInputNotification()
//            sendNotificationBigBitmap()
//            sendNotificationWithProgress()
//            sendNotificationWithCustomView()

        }
    }

    private fun sendNotificationWithCustomView() {
        val pendingIntent = PendingIntent.getActivity(
            this,
            1,
            Intent(this@MainActivity, ResultActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val remoteViews = RemoteViews(packageName, R.layout.notify_view)
        NotificationUtil.createNotificationWithCustomView(
            this@MainActivity,
            R.drawable.ic_launcher_background,
            remoteViews,
            pendingIntent
        )
    }

    private fun sendNotificationWithProgress() {
        NotificationUtil.createNotificationWithProgress(
            this@MainActivity,
            "TIM",
            "正在下载...",
            "下载完成",
            R.drawable.ic_launcher_background,
            100,
            Intent(this@MainActivity, ResultActivity::class.java)
        )
    }

    private fun sendNotification() {
        val pendingIntent = PendingIntent.getActivity(
            this,
            1,
            Intent(this@MainActivity, ResultActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        NotificationUtil.createNotification(
            this@MainActivity,
            "title",
            "content可就是灯笼裤飞机离开房间历史交锋受到了咖啡端上来就螺丝钉解放了决定是否了肯定是距离首都基辅撒啦啦啦啦啦啦啦",
            true,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            pendingIntent
        )
    }

    private fun sendNotificationBigBitmap() {
        val pendingIntent = PendingIntent.getActivity(
            this,
            1,
            Intent(this@MainActivity, ResultActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        NotificationUtil.createNotificationWithBigPicture(
            this@MainActivity,
            "title",
            "content可就是灯笼裤飞机离开房间历史交锋受到了咖啡端上来就螺丝钉解放了决定是否了肯定是距离首都基辅撒啦啦啦啦啦啦啦",
            true,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            BitmapFactory.decodeResource(resources, android.R.drawable.btn_star_big_on),
            pendingIntent
        )
    }

    private fun sendRemoteInputNotification() {
        val intent = Intent(this@MainActivity, SendService::class.java)
        var replyPendingIntent: PendingIntent =
            PendingIntent.getService(
                this@MainActivity,
                Random().nextInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        NotificationUtil.createNotificationWithRemoteInput(
            this@MainActivity,
            "title",
            "content",
            "send",
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            replyPendingIntent
        )
    }

}
