package com.hzy.commonutils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.hzy.utils.ContactsUtil
import com.hzy.utils.NotificationUtil
import com.hzy.utils.VideoUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pendingIntent = PendingIntent.getActivity(this, 1, Intent(this@MainActivity, ResultActivity::class.java), 0)
        tv.setOnClickListener {
            /* NotificationUtil.sendNotification(
                 this,
                 1,
                 "Title",
                 "this is content",
                 R.drawable.ic_launcher_background,
                 R.drawable.ic_launcher_background,
                 System.currentTimeMillis(),
                 "",
                 "123",
                 "channel name",
                 "description",
                 true,
                 pendingIntent
             )*/
            NotificationUtil.createNotification(
                this@MainActivity,
                1,
                "111",
                "channel name",
                "channel description.",
                "title",
                "content可就是灯笼裤飞机离开房间历史交锋受到了咖啡端上来就螺丝钉解放了决定是否了肯定是距离首都基辅撒啦啦啦啦啦啦啦",
                true,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                System.currentTimeMillis(),
                pendingIntent
            )
        }
    }

}
