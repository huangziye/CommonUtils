package com.hzy.commonutils

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.hzy.utils.NotificationUtil
import java.util.*

/**
 *
 * @author: ziye_huang
 * @date: 2019/1/21
 */
class SendService : Service() {
    var resultsIntent: Intent? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        resultsIntent = intent
        // 获取RemoteInput中的Result
        val resultsIntent = RemoteInput.getResultsFromIntent(intent)
        if (null != resultsIntent) {
            val result = resultsIntent.getString(NotificationUtil.RESULT_KEY)
            if (!TextUtils.isEmpty(result)) {
                reply(result!!)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun reply(result: String) {
        val pendingIntent = PendingIntent.getActivity(
            this@SendService,
            Random().nextInt(),
            resultsIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        NotificationUtil.createNotification(
            this@SendService,
            "title",
            result,
            true,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            pendingIntent
        )

        Handler().postDelayed({
            val manger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manger.cancel(NotificationUtil.NOTIFICATION_ID)
        }, 3000)
    }
}