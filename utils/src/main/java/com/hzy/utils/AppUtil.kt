package com.hzy.utils

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.annotation.RequiresPermission
import androidx.core.content.FileProvider
import java.io.File


/**
 * App相关工具类
 * @author: ziye_huang
 * @date: 2019/5/9
 */
object AppUtil {

    /**
     * 获取设备的制造商
     *
     * @return 设备制造商
     */
    fun getDeviceManufacture(): String {
        return Build.MANUFACTURER
    }

    /**
     * 获取设备名称
     *
     * @return 设备名称
     */
    fun getDeviceName(): String {
        return Build.MODEL
    }

    /**
     * 获取系统版本号
     *
     * @return 系统版本号
     */
    fun getSystemVersion(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * 获取设备号
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getDeviceIMEI(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (telephonyManager == null || TextUtils.isEmpty(telephonyManager.deviceId)) {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } else {
            telephonyManager.deviceId
        }
    }

    /**
     * 获取emei集合
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getDeviceIMEIList(context: Context): List<String> {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val list = mutableListOf<String>()
        if (telephonyManager == null || TextUtils.isEmpty(telephonyManager.deviceId)) {//如果获取deviceId为null，就获取androidID
            list.add(getAndroidID(context))
            return list
        } else {
            var phoneCount = 1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                phoneCount = telephonyManager.phoneCount
                for (i in 0 until phoneCount) {
                    list.add(telephonyManager.getDeviceId(i))
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    list.add(telephonyManager.meid)
                }
            } else {
                list.add(telephonyManager.deviceId)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    list.add(telephonyManager.meid)
                }
            }
            list.add(getAndroidID(context))
            return list
        }
    }

    /**
     * 获取androidID
     *
     * @param context
     * @return
     */
    fun getAndroidID(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * 获取应用的版本号
     *
     * @param context
     * @return
     */
    fun getAppVersion(context: Context): String? {
        val packageManager = context.packageManager
        val packageInfo: PackageInfo
        try {
            packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取应用程序名称
     *
     * @param context
     * @return
     */
    fun getAppName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 是否是主线程
     *
     * @return
     */
    fun isMainThread(): Boolean {
        return Looper.getMainLooper() == Looper.myLooper()
    }

    private val PACKAGE_URL_SCHEME = "package:" // 方案

    /**
     * 启动应用的设置
     */
    fun startAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse(PACKAGE_URL_SCHEME + context.packageName)
        context.startActivity(intent)
    }

    /**
     * 判断服务是否工作
     *
     * @param context
     * @param serviceName
     * @return
     */
    fun isServiceWorked(context: Context, serviceName: String): Boolean {
        val myManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningService =
            myManager.getRunningServices(Integer.MAX_VALUE) as ArrayList<ActivityManager.RunningServiceInfo>
        for (i in 0 until runningService.size) {
            if (runningService[i].service.className.toString() == serviceName) {
                return true
            }
        }
        return false
    }

    /**
     * 检查手机是否安装了app
     *
     * @param context
     * @param packageName
     * @return
     */
    fun checkApkExist(context: Context, packageName: String?): Boolean {
        if (packageName == null || "" == packageName)
            return false
        return try {
            val info =
                context.packageManager.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }

    /**
     * 安装app
     *
     * @param context
     * @param file
     * @param authority
     */
    fun installApp(context: Context, file: File?, authority: String) {
        if (file == null)
            return
        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri
        val type = "application/vnd.android.package-archive"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file)
        } else {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            data = FileProvider.getUriForFile(context, authority, file)
        }
        intent.setDataAndType(data, type)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {

        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = Integer.parseInt(clazz.getField("status_bar_height").get(`object`).toString())
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusHeight
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    fun snapshotWithStatusBar(activity: Activity): Bitmap? {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val width = getScreenWidth(activity)
        val height = getScreenHeight(activity)
        var bp: Bitmap?
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height)
        view.destroyDrawingCache()
        return bp

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    fun snapshotWithoutStatusBar(activity: Activity): Bitmap? {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top
        val width = getScreenWidth(activity)
        val height = getScreenHeight(activity)
        var bp: Bitmap?
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight)
        view.destroyDrawingCache()
        return bp
    }

}