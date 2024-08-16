package com.hzy.commonutils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hzy.utils.*
import kotlinx.android.synthetic.main.activity_bottom_navigation.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = MainActivity::class.java.simpleName

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
//        StatusBarUtil.setStatusBarVisible(this, show)
        btn_test.setOnClickListener(this)
        btn_status_bar.setOnClickListener(this)

//        TextViewUtil.setTextColor(this, tv, R.color.colorAccent, "同样在Kotlin中会有List、Map、Set，而与Java中数据结构大多相同，也略有区别。Kotlin中的集合分为可变集合与不可变集合。", false, "略有区别", "不可变集合", "同样在")
        TextViewUtil.setTextColor(
            this,
            tv,
            R.color.colorAccent,
            "同样在Kotlin中会有List、Map、Set，而与Java中数据结构大多相同，也略有区别。Kotlin中的集合分为可变集合与不可变集合。",
            false,
            Pair<String, TextViewUtil.SubTextClickableSpan>("略有区别", object: TextViewUtil.SubTextClickableSpan(this, R.color.colorAccent) {
                override fun onClick(widget: View) {
                    Toast.makeText(this@MainActivity, "略有区别", Toast.LENGTH_LONG).show()
                }
            }),
            Pair<String, TextViewUtil.SubTextClickableSpan>("不可变集合", object: TextViewUtil.SubTextClickableSpan(this, R.color.colorAccent) {
                override fun onClick(widget: View) {
                    Toast.makeText(this@MainActivity, "不可变集合", Toast.LENGTH_LONG).show()
                }
            }),
            Pair<String, TextViewUtil.SubTextClickableSpan>("同样在", object: TextViewUtil.SubTextClickableSpan(this, R.color.colorAccent) {
                override fun onClick(widget: View) {
                    Toast.makeText(this@MainActivity, "同样在", Toast.LENGTH_LONG).show()
                }
            })
        )
        /*TextViewUtil.setTextColor(
            this,
            tv,
            R.color.colorAccent,
            "同样在Kotlin中会有List、Map、Set，而与Java中数据结构大多相同，也略有区别。Kotlin中的集合分为可变集合与不可变集合。",
            false,
            8,
            20)*/

        /*TextViewUtil.setTextColor(
            this,
            tv,
            R.color.colorAccent,
            "同样在Kotlin中会有List、Map、Set，而与Java中数据结构大多相同，也略有区别。Kotlin中的集合分为可变集合与不可变集合。",
            false,
            8,
            20,
            object : TextViewUtil.SubTextClickableSpan(this, R.color.colorAccent) {
                override fun onClick(widget: View) {
                    Toast.makeText(this@MainActivity, widget.toString(), Toast.LENGTH_LONG).show()
                }
            })*/
        /*TextViewUtil.setTextColor(
            this,
            tv,
            R.color.colorAccent,
            "同样在Kotlin中会有List、Map、Set，而与Java中数据结构大多相同，也略有区别。Kotlin中的集合分为可变集合与不可变集合。",
            false,
            TextViewUtil.SubTextClickListener(
                TextViewUtil.SubTextClickableSpan(this, R.color.colorAccent, View.OnClickListener { Log.e(TAG, "1") }),
                TextViewUtil.SubTextClickableSpan(this, R.color.colorAccent, View.OnClickListener { Log.e(TAG, "2") }),
                TextViewUtil.SubTextClickableSpan(this, R.color.colorAccent, View.OnClickListener { Log.e(TAG, "3") })
            ),
            "略有区别", "不可变集合", "同样在"
        )*/
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_test -> {
                //            sendNotification()
//            sendRemoteInputNotification()
//            sendNotificationBigBitmap()
//            sendNotificationWithProgress()
//            sendNotificationWithCustomView()

//            CameraUtil.takePhoto(this@MainActivity, packageName, "output.png")
//            CameraUtil.choosePhoto(this@MainActivity)

                startActivity(Intent(this, BottomNavigationActivity::class.java))
            }
            R.id.btn_status_bar -> {
                startActivity(Intent(this, StatusBarActivity::class.java))
            }
        }
    }

    /*private fun countdownTimer() {
        var countDownTimerUtil = CountDownTimerUtil(this, 10000, 1000, count)
        countDownTimerUtil
            .onTickTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            .onFinishTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)).onPrefixFinishText("剩余")
            .onSuffixFinishText("秒")
        countDownTimerUtil.start()

        Handler().postDelayed({
            countDownTimerUtil.close()
        }, 5000)
    }*/

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
