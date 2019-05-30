package com.hzy.utils

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
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
        return if (TextUtils.isEmpty(telephonyManager.deviceId)) {
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
        if (TextUtils.isEmpty(telephonyManager.deviceId)) {//如果获取deviceId为null，就获取androidID
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
     * 获取应用的版本名称
     *
     * @param context
     * @return
     */
    fun getAppVersionName(context: Context): String? {
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
     * 获取应用的版本号
     *
     * @param context
     * @return
     */
    fun getAppVersionCode(context: Context): Int {
        val packageManager = context.packageManager
        val packageInfo: PackageInfo
        try {
            packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 1
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
     * 获取应用程序包名
     */
    fun getAppPackageName(context: Context): String {
        return context.applicationContext.packageName
    }

    /**
     * 获取所有安装的应用程序,不包含系统应用
     */
    fun getInstalledPackages(context: Context): List<PackageInfo> {
        val packageInfos = context.packageManager.getInstalledPackages(0)
        val installedList = mutableListOf<PackageInfo>()
        for (i in packageInfos.indices) {
            if (packageInfos[i].applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                installedList.add(packageInfos[i])
            }
        }
        return installedList
    }

    /**
     * 获取应用程序的icon图标
     * @param context
     * @return
     * 当包名错误时，返回null
     */
    fun getApplicationIcon(context: Context): Drawable? {
        val packageManager = context.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.applicationInfo.loadIcon(packageManager)
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
     * 启动安装应用程序(兼容Android7.0) https://www.jianshu.com/p/3c554d3983d8
     * @param apkPath  应用程序路径
     */
    fun installApk(context: Activity, apkPath: String): Boolean {
        try {
            if (TextUtils.isEmpty(apkPath)) {
                "apk路径为空".toast(context)
                return false
            }
            val file = File(apkPath)
            if (!file.exists()) {
                Toast.makeText(context, "apk文件不存在", Toast.LENGTH_SHORT).show()
                return false
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)//增加读写权限
            }
            intent.setDataAndType(getPathUri(context, apkPath), "application/vnd.android.package-archive")
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            "安装失败，请重新下载".toast(context)
            return false
        } catch (error: Error) {
            error.printStackTrace()
            "安装失败，请重新下载".toast(context)
            return false
        }
        return true
    }

    fun getPathUri(context: Context, filePath: String): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val packageName = context.packageName
            FileProvider.getUriForFile(context, "$packageName.fileprovider", File(filePath))
        } else {
            Uri.fromFile(File(filePath))
        }
    }

    /**
     * 获取渠道名
     * @param context
     * @return
     */
    fun getChannel(context: Context): String? {
        return try {
            val pm = context.packageManager
            val appInfo = pm.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            appInfo.metaData.getString("UMENG_CHANNEL")
        } catch (ignored: PackageManager.NameNotFoundException) {
            ""
        }
    }

    private var firstTime: Long = 0

    /**
     * 设置连续点击返回后退出该应用
     * @param context
     * @param rightNow 立刻退出应用
     */
    fun exitApp(context: Activity, rightNow: Boolean) {
        val secondTime = System.currentTimeMillis()
        if(rightNow) context.finish()
        if (secondTime - firstTime > 2000) {
            "再按一次退出程序".toast(context)
            firstTime = secondTime
        } else {
            context.finish()
        }
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