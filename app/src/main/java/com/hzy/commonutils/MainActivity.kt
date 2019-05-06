package com.hzy.commonutils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hzy.utils.CameraUtil
import com.hzy.utils.CountDownTimerUtil
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    var show = false

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
//        StatusBarUtil.setStatusBarVisible(this, show)
        var countDownTimerUtil = CountDownTimerUtil(this, 10000, 1000, count)
        tv.setOnClickListener {
            //            sendNotification()
//            sendRemoteInputNotification()
//            sendNotificationBigBitmap()
//            sendNotificationWithProgress()
//            sendNotificationWithCustomView()

//            CameraUtil.takePhoto(this@MainActivity, packageName, "output.png")

//            CameraUtil.choosePhoto(this@MainActivity)
            show = !show
            countDownTimerUtil
                .onTickTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .onFinishTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)).onPrefixFinishText("剩余")
                .onSuffixFinishText("秒")
            countDownTimerUtil.start()


            Handler().postDelayed({
                countDownTimerUtil.close()
            }, 5000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CameraUtil.TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            iv.setImageBitmap(CameraUtil.getTakePhoto(this@MainActivity))
        } else if (requestCode == CameraUtil.CHOOSE_PHOTO && resultCode == Activity.RESULT_OK) {
            iv.setImageBitmap(BitmapFactory.decodeFile(CameraUtil.getChoosePhoto(this@MainActivity, intent)))
        }
    }

    /*private fun sendNotificationWithCustomView() {
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
    }*/

    /*private fun sendNotificationWithProgress() {
        NotificationUtil.createNotificationWithProgress(
            this@MainActivity,
            "TIM",
            "正在下载...",
            "下载完成",
            R.drawable.ic_launcher_background,
            100,
            Intent(this@MainActivity, ResultActivity::class.java)
        )
    }*/

    /*private fun sendNotification() {
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
    }*/

}
