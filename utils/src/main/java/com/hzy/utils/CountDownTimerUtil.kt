package com.hzy.utils

import android.content.Context
import android.os.CountDownTimer
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

/**
 * 倒计时
 *
 * @param millisInFuture 总时长
 * @param countDownInterval 计时的时间间隔
 *
 * @author: ziye_huang
 * @date: 2019/4/2
 */
class CountDownTimerUtil(
    val context: Context,
    val millisInFuture: Long,
    val countDownInterval: Long,
    val textView: TextView
) :
    CountDownTimer(millisInFuture, countDownInterval) {

    /**
     * 在倒计时过程中的字体颜色
     */
    @ColorInt
    private var mTickTextColor: Int
    /**
     * 在倒计时结束时的字体颜色
     */
    private var mFinishTextColor: Int
    /**
     * 在倒计时过程中的背景颜色
     */
    @ColorInt
    private var mTickBackgroundColor: Int
    /**
     * 在倒计结束时的背景颜色
     */
    @ColorInt
    private var mFinishBackgroundColor: Int
    /**
     * 在倒计时过程中的字体大小
     */
    private var mTickTextSize: Int
    /**
     * 在倒计结束时的字体大小
     */
    private var mFinishTextSize: Int

    init {
        mTickTextColor = ContextCompat.getColor(context, R.color.default_text_color)
        mFinishTextColor = ContextCompat.getColor(context, R.color.default_text_color)

        mTickBackgroundColor = ContextCompat.getColor(context, android.R.color.white)
        mFinishBackgroundColor = ContextCompat.getColor(context, android.R.color.white)

        mTickTextSize = DensityUtil.px2sp(context, context.resources.getDimension(R.dimen.default_text_size))
        mFinishTextSize = DensityUtil.px2sp(context, context.resources.getDimension(R.dimen.default_text_size))

    }

    /**
     * 计时完毕时触发
     */
    override fun onFinish() {
        textView.isClickable = true
        updateOnFinishParams()
        textView.text = "重新发送"
    }

    /**
     * 计时过程显示
     */
    override fun onTick(millisUntilFinished: Long) {
        //设置不可点击
        textView.isClickable = false
        updateOnTickParams()
        //设置倒计时时间
        textView.text = "${millisUntilFinished / 1000}s"
    }

    /**
     * 更新倒计时结束的参数
     */
    private fun updateOnFinishParams() {
        textView.setTextColor(mFinishTextColor)
        textView.setBackgroundColor(mFinishBackgroundColor)
        textView.textSize = mFinishTextSize.toFloat()
    }

    /**
     * 更新倒计时过程中的参数
     */
    private fun updateOnTickParams() {
        textView.setTextColor(mTickTextColor)
        textView.setBackgroundColor(mTickBackgroundColor)
        textView.textSize = mTickTextSize.toFloat()
    }

    fun onTickTextColor(@ColorInt color: Int): CountDownTimerUtil {
        mTickTextColor = color
        return this
    }

    fun onFinishTextColor(@ColorInt color: Int): CountDownTimerUtil {
        mFinishTextColor = color
        return this
    }

    fun onTickBackgroundColor(@ColorInt color: Int): CountDownTimerUtil {
        mTickBackgroundColor = color
        return this
    }

    fun onFinishBackgroundColor(@ColorInt color: Int): CountDownTimerUtil {
        mFinishBackgroundColor = color
        return this
    }

    fun onTickTextSize(textSize: Int): CountDownTimerUtil {
        mTickTextSize = textSize
        return this
    }

    fun onFinishTextSize(textSize: Int): CountDownTimerUtil {
        mFinishTextSize = textSize
        return this
    }
}