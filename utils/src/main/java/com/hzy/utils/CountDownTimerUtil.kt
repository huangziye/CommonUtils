package com.hzy.utils

import android.annotation.SuppressLint
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
    private var mTickTextColor: Int = -1
    /**
     * 在倒计时结束时的字体颜色
     */
    private var mFinishTextColor: Int = -1
    /**
     * 在倒计时过程中的背景颜色
     */
    @ColorInt
    private var mTickBackgroundColor: Int = -1
    /**
     * 在倒计结束时的背景颜色
     */
    @ColorInt
    private var mFinishBackgroundColor: Int = -1
    /**
     * 在倒计时过程中的字体大小
     */
    private var mTickTextSize: Int = -1
    /**
     * 在倒计结束时的字体大小
     */
    private var mFinishTextSize: Int = -1
    /**
     * 倒计时结束显示的文字
     */
    private var mFinishText: String = "重新发送"
    /**
     * 倒计时结束显示的文字前缀
     */
    private var mPrefixFinishText = ""
    /**
     * 倒计时结束显示的文字后缀
     */
    private var mSuffixFinishText = ""


    /**
     * 计时完毕时触发
     */
    override fun onFinish() {
        textView.isClickable = true
        updateOnFinishParams()
        textView.text = mFinishText
    }

    /**
     * 计时过程显示
     */
    @SuppressLint("SetTextI18n")
    override fun onTick(millisUntilFinished: Long) {
        //设置不可点击
        textView.isClickable = false
        updateOnTickParams()
        //设置倒计时时间
        textView.text = mPrefixFinishText + "${millisUntilFinished / 1000}" + mSuffixFinishText
    }

    /**
     * 更新倒计时结束的参数
     */
    private fun updateOnFinishParams() {
        if (mFinishTextColor != -1) textView.setTextColor(mFinishTextColor)
        if (mFinishBackgroundColor != -1) textView.setBackgroundColor(mFinishBackgroundColor)
        if (mFinishTextSize != -1) textView.textSize = mFinishTextSize.toFloat()
    }

    /**
     * 更新倒计时过程中的参数
     */
    private fun updateOnTickParams() {
        if (mTickTextColor != -1) textView.setTextColor(mTickTextColor)
        if (mTickBackgroundColor != -1) textView.setBackgroundColor(mTickBackgroundColor)
        if (mTickTextSize != -1) textView.textSize = mTickTextSize.toFloat()
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

    fun onFinishText(text: String): CountDownTimerUtil {
        mFinishText = text
        return this
    }

    fun onPrefixFinishText(prefix: String): CountDownTimerUtil {
        mPrefixFinishText = prefix
        return this
    }

    fun onSuffixFinishText(suffix: String): CountDownTimerUtil {
        mSuffixFinishText = suffix
        return this
    }
}