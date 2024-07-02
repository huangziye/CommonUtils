package com.hzy.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.*
import android.util.Log
import androidx.annotation.RequiresPermission
import com.hzy.utils.constant.NetworkType
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.UnknownHostException


/**
 * 网络相关工具类
 *
 *  * 需添加的权限：
 * {@code <uses-permission android:name="android.permission.INTERNET"/>}
 * {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>}
 * {@code <uses-permission android:name="android.permission.MODIFY_PHONE_STATE"/>}
 * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}
 *
 * @author: ziye_huang
 * @date: 2019/1/25
 */
object NetworkUtil {

    private val TAG = NetworkUtil::class.java.javaClass.simpleName

    /**
     * 获取活动网络信息
     *
     * @return NetworkInfo
     */
    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
    }

    /**
     * 判断网络是否连接
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isConnected(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * 判断网络是否可用
     *
     * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
     *
     * 需要异步 ping，如果 ping 不通就说明网络不可用
     *
     * ping 的 ip 为阿里巴巴公共 ip：223.5.5.5
     *
     * @return `true`: 可用<br></br>`false`: 不可用
     */
    @RequiresPermission("android.permission.INTERNET")
    fun isAvailableByPing(): Boolean {
        return isAvailableByPing(null)
    }

    /**
     * 判断网络是否可用
     *
     * 需要异步 ping，如果 ping 不通就说明网络不可用
     *
     * @param ip ip 地址（自己服务器 ip），如果为空，ip 为阿里巴巴公共 ip
     * @return `true`: 可用<br></br>`false`: 不可用
     */
    fun isAvailableByPing(ip: String?): Boolean {
        var ip = ip
        if (ip == null || ip.isEmpty()) {
            // 阿里巴巴公共 ip
            ip = "223.5.5.5"
        }
        val result = ShellUtil.execCmd(String.format("ping -c 1 %s", ip), false)
        val ret = result.result == 0
        Log.d(TAG, "isAvailableByPing() called" + result.successMsg)
        Log.d(TAG, "isAvailableByPing() called" + result.errorMsg)
        return ret
    }

    /**
     * 判断 wifi 是否打开
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isWifiEnabled(context: Context): Boolean {
        @SuppressLint("WifiManagerLeak")
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }

    /**
     * 打开或关闭 wifi
     *
     * @param enabled `true`: 打开<br></br>`false`: 关闭
     */
    fun setWifiEnabled(context: Context, enabled: Boolean) {
        @SuppressLint("WifiManagerLeak")
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (enabled) {
            if (!wifiManager.isWifiEnabled) {
                wifiManager.isWifiEnabled = true
            }
        } else {
            if (wifiManager.isWifiEnabled) {
                wifiManager.isWifiEnabled = false
            }
        }
    }

    /**
     * 判断 wifi 是否连接
     *
     * @return `true`: 连接<br></br>`false`: 未连接
     */
    fun isWifiConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return (cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.type == ConnectivityManager.TYPE_WIFI)
    }

    /**
     * 判断 wifi 数据是否可用
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    @RequiresPermission("android.permission.INTERNET")
    fun isWifiAvailable(context: Context): Boolean {
        return isWifiEnabled(context) && isAvailableByPing()
    }

    /**
     * 判断移动数据是否打开
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isMobileDataEnabled(context: Context): Boolean {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val getMobileDataEnabledMethod = tm.javaClass.getDeclaredMethod("getDataEnabled")
            if (getMobileDataEnabledMethod != null) {
                return getMobileDataEnabledMethod.invoke(tm) as Boolean
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 打开或关闭移动数据
     *
     * @param enabled `true`: 打开<br></br>`false`: 关闭
     */
    fun setMobileDataEnabled(context: Context, enabled: Boolean) {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val setMobileDataEnabledMethod = tm.javaClass.getDeclaredMethod("setDataEnabled", Boolean::class.javaPrimitiveType)
            setMobileDataEnabledMethod?.invoke(tm, enabled)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取网络运营商名称
     *
     * 中国移动、如中国联通、中国电信
     *
     * @return 运营商名称
     */
    fun getNetworkOperatorName(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkOperatorName
    }

    /**
     * 获取当前网络类型
     *
     * @return 网络类型
     *
     *  * [NetworkType.NETWORK_WIFI]
     *  * [NetworkType.NETWORK_4G]
     *  * [NetworkType.NETWORK_3G]
     *  * [NetworkType.NETWORK_2G]
     *  * [NetworkType.NETWORK_UNKNOWN]
     *  * [NetworkType.NETWORK_NO]
     *
     */
    fun getNetworkType(context: Context): NetworkType {
        var netType = NetworkType.NETWORK_NO
        val info = getActiveNetworkInfo(context)
        if (info != null && info.isAvailable) {
            when {
                info.type == ConnectivityManager.TYPE_WIFI -> netType = NetworkType.NETWORK_WIFI
                info.type == ConnectivityManager.TYPE_MOBILE -> when (info.subtype) {

                    NETWORK_TYPE_GSM, TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> netType =
                        NetworkType.NETWORK_2G

                    NETWORK_TYPE_TD_SCDMA, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> netType =
                        NetworkType.NETWORK_3G

                    NETWORK_TYPE_IWLAN, TelephonyManager.NETWORK_TYPE_LTE -> netType =
                        NetworkType.NETWORK_4G
                    else -> {

                        val subtypeName = info.subtypeName
                        //  中国移动 联通 电信 三种 3G 制式
                        netType = if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                            || subtypeName.equals("WCDMA", ignoreCase = true)
                            || subtypeName.equals("CDMA2000", ignoreCase = true)
                        ) {
                            NetworkType.NETWORK_3G
                        } else {
                            NetworkType.NETWORK_UNKNOWN
                        }
                    }
                }
                else -> netType = NetworkType.NETWORK_UNKNOWN
            }
        }
        return netType
    }

    /**
     * 通过 wifi 获取本地 IP 地址
     *
     * @return IP 地址
     */
    fun getIpAddressByWifi(context: Context): String {
        // 获取wifi服务
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress
        return intToIp(ipAddress)
    }

    private fun intToIp(i: Int): String {
        return (i and 0xFF).toString() + "." +
                (i shr 8 and 0xFF) + "." +
                (i shr 16 and 0xFF) + "." +
                (i shr 24 and 0xFF)
    }

    /**
     * 获取 IP 地址
     *
     * @param useIPv4 是否用 IPv4
     * @return IP 地址
     */
    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                // 防止小米手机返回 10.0.2.15
                if (!ni.isUp) continue
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        val hostAddress = inetAddress.hostAddress
                        val isIPv4 = hostAddress.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return hostAddress
                        } else {
                            if (!isIPv4) {
                                val index = hostAddress.indexOf('%')
                                return if (index < 0) hostAddress.toUpperCase() else hostAddress.substring(
                                    0,
                                    index
                                ).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取域名 IP 地址
     *
     * @param domain 域名
     * @return IP 地址
     */
    fun getDomainAddress(domain: String): String? {
        return try {
            InetAddress.getByName(domain).hostAddress
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            null
        }
    }
}