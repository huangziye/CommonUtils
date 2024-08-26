package com.hzy.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi

/**
 *
 */
object TextViewUtil {

    /**
     * 设置文本子串颜色
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun setTextColor(context: Context, tv: TextView, text: String, @ColorRes textColor: Int, subTextShowUnderline: Boolean, vararg subText: String) {
        var ss = SpannableString(text)
        for (st in subText) {
            val indexs = getSubTextIndex(text, st)
            for (startIndex in indexs) {
                val endIndex = startIndex + st.length
                ss = setForegroundColorSpan(context, ss, textColor, startIndex, endIndex)
                if (subTextShowUnderline) {
                    ss = setSubTextShowUnderline(ss, startIndex, endIndex)
                }
            }
        }
        // 使超链接生效
        tv.movementMethod = LinkMovementMethod.getInstance()
        // 高亮颜色
        tv.highlightColor = Color.TRANSPARENT
        tv.text = ss
    }

    /**
     * 设置文本子串颜色
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun setTextColor(
        context: Context,
        tv: TextView,
        text: String,
        @ColorRes textColor: Int,
        subTextShowUnderline: Boolean,
        vararg subTextPair: Pair<String, SubTextClickableSpan>
    ) {
        var ss = SpannableString(text)
        for (pair in subTextPair) {
            val (subText, subTextClickableSpan) = pair
            val indexs = getSubTextIndex(text, subText)
            for (startIndex in indexs) {
                val endIndex = startIndex + subText.length
                ss = setForegroundColorSpan(context, ss, textColor, startIndex, endIndex)
                if (subTextShowUnderline) {
                    ss = setSubTextShowUnderline(ss, startIndex, endIndex)
                }
                ss.setSpan(subTextClickableSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }
        // 使超链接生效
        tv.movementMethod = LinkMovementMethod.getInstance()
        // 高亮颜色
        tv.highlightColor = Color.TRANSPARENT
        tv.text = ss
    }

    /**
     * 设置文本子串颜色
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun setTextColor(
        context: Context,
        tv: TextView,
        text: String,
        @ColorRes textColor: Int,
        subTextShowUnderline: Boolean,
        startIndex: Int,
        endIndex: Int,
        subTextClickableSpan: SubTextClickableSpan? = null
    ) {
        var ss = SpannableString(text)
        ss = setForegroundColorSpan(context, ss, textColor, startIndex, endIndex)
        if (subTextShowUnderline) {
            ss = setSubTextShowUnderline(ss, startIndex, endIndex)
        }
        subTextClickableSpan?.let { ss.setSpan(subTextClickableSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE) }
        // 使超链接生效
        tv.movementMethod = LinkMovementMethod.getInstance()
        // 高亮颜色
        tv.highlightColor = Color.TRANSPARENT
        tv.text = ss
    }

    /**
     * 设置文字前景色
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setForegroundColorSpan(context: Context, ss: SpannableString, @ColorRes textColor: Int, startIndex: Int, endIndex: Int): SpannableString {
        ss.setSpan(ForegroundColorSpan(context.getColor(textColor)), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 设置TextView的文本是否显示下划线
     */
    private fun setShowUnderline(tv: TextView) {
        tv.paint.isUnderlineText = true
    }

    /**
     * 设置TextView的子串文本是否显示下划线
     */
    private fun setSubTextShowUnderline(ss: SpannableString, startIndex: Int, endIndex: Int): SpannableString {
        ss.setSpan(UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 获取SubText出现的索引集合
     */
    private fun getSubTextIndex(text: String, subText: String): List<Int> {
        val list = mutableListOf<Int>()
        var index = text.indexOf(subText)
        while (index >= 0) {
            list.add(index)
            index = text.indexOf(subText, index + 1)
        }
        return list
    }

    open class SubTextClickableSpan(private var context: Context, @ColorRes private var clickedColor: Int?) : ClickableSpan() {

        override fun onClick(widget: View) {
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun updateDrawState(ds: TextPaint) {
            clickedColor?.let { ds.color = context.getColor(clickedColor!!) }
        }
    }
}