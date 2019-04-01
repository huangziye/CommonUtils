package com.hzy.utils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.core.widget.TextViewCompat
import java.lang.ref.WeakReference

/**
 * 吐司相关工具类
 * @author ziye_huang
 */
object ToastUtil {
    private var COLOR_DEFAULT = -0x1000001
    private var HANDLER = Handler(Looper.getMainLooper())

    private var mToast: Toast? = null
    private var mViewWeakReference: WeakReference<View>? = null
    private var mLayoutId = -1
    private var mGravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
    private var mXOffset = 0
    private var mYOffset = 0
    private var mBgColor = COLOR_DEFAULT
    private var mBgResource = -1
    private var mMsgColor = COLOR_DEFAULT


    /**
     * 设置吐司位置
     *
     * @param gravity 位置
     * @param xOffset x偏移
     * @param yOffset y偏移
     */
    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        ToastUtil.mGravity = gravity
        ToastUtil.mXOffset = xOffset
        ToastUtil.mYOffset = yOffset
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor 背景色
     */
    fun setBgColor(@ColorInt backgroundColor: Int) {
        ToastUtil.mBgColor = backgroundColor
    }

    /**
     * 设置背景资源
     *
     * @param bgResource 背景资源
     */
    fun setBgResource(@DrawableRes bgResource: Int) {
        ToastUtil.mBgResource = bgResource
    }

    /**
     * 设置消息颜色
     *
     * @param msgColor 颜色
     */
    fun setMsgColor(@ColorInt msgColor: Int) {
        ToastUtil.mMsgColor = msgColor
    }

    /**
     * 安全地显示短时吐司
     *
     * @param text 文本
     */
    fun showShort(context: Context, @NonNull text: CharSequence) {
        show(context, text, Toast.LENGTH_SHORT)
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     */
    fun showShort(context: Context, @StringRes resId: Int) {
        show(context, resId, Toast.LENGTH_SHORT)
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    fun showShort(context: Context, @StringRes resId: Int, vararg args: Any) {
        show(context, resId, Toast.LENGTH_SHORT, *args)
    }

    /**
     * 安全地显示短时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    fun showShort(context: Context, format: String, vararg args: Any) {
        show(format, Toast.LENGTH_SHORT, *args)
    }

    /**
     * 安全地显示长时吐司
     *
     * @param text 文本
     */
    fun showLong(context: Context, @NonNull text: CharSequence) {
        show(context, text, Toast.LENGTH_LONG)
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     */
    fun showLong(context: Context, @StringRes resId: Int) {
        show(context, resId, Toast.LENGTH_LONG)
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    fun showLong(context: Context, @StringRes resId: Int, vararg args: Any) {
        show(context, resId, Toast.LENGTH_LONG, *args)
    }

    /**
     * 安全地显示长时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    fun showLong(context: Context, format: String, vararg args: Any) {
        show(format, Toast.LENGTH_LONG, *args)
    }

    /**
     * 安全地显示短时自定义吐司
     */
    fun showCustomShort(context: Context, @LayoutRes layoutId: Int): View {
        val view = getView(context, layoutId)
        show(context, view, Toast.LENGTH_SHORT)
        return view
    }

    /**
     * 安全地显示长时自定义吐司
     */
    fun showCustomLong(context: Context, @LayoutRes layoutId: Int): View {
        val view = getView(context, layoutId)
        show(context, view, Toast.LENGTH_LONG)
        return view
    }

    /**
     * 取消吐司显示
     */
    fun cancel() {
        if (mToast != null) {
            mToast!!.cancel()
            mToast = null
        }
    }

    private fun show(context: Context, @StringRes resId: Int, duration: Int) {
        show(context.resources.getText(resId).toString(), duration)
    }

    private fun show(context: Context, @StringRes resId: Int, duration: Int, vararg args: Any) {
        show(String.format(context.resources.getString(resId), args), duration)
    }

    private fun show(format: String, duration: Int, vararg args: Any) {
        show(String.format(format, *args), duration)
    }

    private fun show(context: Context, text: CharSequence, duration: Int) {
        HANDLER.post {
            cancel()
            mToast = Toast.makeText(context, text, duration)
            // solve the font of toast
            val tvMessage = mToast!!.view.findViewById<View>(android.R.id.message) as TextView
            TextViewCompat.setTextAppearance(tvMessage, android.R.style.TextAppearance)
            tvMessage.setTextColor(mMsgColor)
            setBgAndGravity(context)
            mToast!!.show()
        }
    }

    private fun show(context: Context, view: View, duration: Int) {
        HANDLER.post(Runnable {
            cancel()
            mToast = Toast(context)
            mToast!!.view = view
            mToast!!.duration = duration
            setBgAndGravity(context)
            mToast!!.show()
        })
    }

    private fun setBgAndGravity(context: Context) {
        mYOffset = (64 * context.resources.displayMetrics.density + 0.5).toInt()
        val toastView = mToast!!.view
        if (mBgResource != -1) {
            toastView.setBackgroundResource(mBgResource)
        } else if (mBgColor != COLOR_DEFAULT) {
            val background = toastView.background
            background.colorFilter = PorterDuffColorFilter(mBgColor, PorterDuff.Mode.SRC_IN)
        }
        mToast!!.setGravity(mGravity, mXOffset, mYOffset)
    }

    private fun getView(context: Context, @LayoutRes layoutId: Int): View {
        if (mLayoutId == layoutId) {
            if (mViewWeakReference != null) {
                val toastView = mViewWeakReference!!.get()
                if (toastView != null) {
                    return toastView
                }
            }
        }
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView = inflate.inflate(layoutId, null)
        mViewWeakReference = WeakReference(toastView)
        mLayoutId = layoutId
        return toastView
    }
}