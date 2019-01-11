package com.hzy.commonutils

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.hzy.utils.VideoUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv.setImageBitmap(VideoUtil.getNetVideoBitmap("http://192.168.0.222:8080/a.mp4", 20000))
        val localPath = "${Environment.getExternalStorageDirectory()}/a.mp4"
        tv.text = VideoUtil.getLocalVideoFileLength(localPath).toString()
    }
}
