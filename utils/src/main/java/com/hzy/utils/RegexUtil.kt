package com.hzy.utils

import android.text.TextUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 *
 * @author: ziye_huang
 * @date: 2019/5/6
 */
object RegexUtil {

    //邮箱表达式
    private val EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")

    //手机号表达式
    private val PHONE_PATTERN = Pattern.compile("^(1)\\d{10}$")

    //银行卡号表达式
    private val BANKNO_PATTERN = Pattern.compile("^[0-9]{16,19}$")

    //座机号码表达式
    private val PLANE_PATTERN =
        Pattern.compile("^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?$")

    //非零表达式
    private val NOTZERO_PATTERN = Pattern.compile("^\\+?[1-9][0-9]*$")

    //数字表达式
    private val NUMBER_PATTERN = Pattern.compile("^[0-9]*$")

    //大写字母表达式
    private val UPCHAR_PATTERN = Pattern.compile("^[A-Z]+$")

    //小写字母表达式
    private val LOWCHAR_PATTERN = Pattern.compile("^[a-z]+$")

    //大小写字母表达式
    private val LETTER_PATTERN = Pattern.compile("^[A-Za-z]+$")

    //中文汉字表达式
    private val CHINESE_PATTERN = Pattern.compile("^[\u4e00-\u9fa5],{0,}$")

    //条形码表达式
    private val ONECODE_PATTERN = Pattern.compile("^(([0-9])|([0-9])|([0-9]))\\d{10}$")

    //邮政编码表达式
    private val POSTALCODE_PATTERN = Pattern.compile("([0-9]{3})+.([0-9]{4})+")

    //IP地址表达式
    private val IPADDRESS_PATTERN =
        Pattern.compile("[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))")

    //URL地址表达式
    private val URL_PATTERN =
        Pattern.compile("(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?")

    //用户名表达式
    private val USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{1}[A-Za-z0-9_.-]{3,31}")

    //真实姓名表达式
    private val REALNEM_PATTERN = Pattern.compile("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*")

    //匹配HTML标签,通过下面的表达式可以匹配出HTML中的标签属性。
    private val HTML_PATTER =
        Pattern.compile("<\\\\/?\\\\w+((\\\\s+\\\\w+(\\\\s*=\\\\s*(?:\".*?\"|'.*?'|[\\\\^'\">\\\\s]+))?)+\\\\s*|\\\\s*)\\\\/?>")

    //抽取注释,如果你需要移除HMTL中的注释，可以使用如下的表达式。
    private val NOTES_PATTER = Pattern.compile("<!--(.*?)-->")

    //查找CSS属性,通过下面的表达式，可以搜索到相匹配的CSS属性。
    private val CSS_PATTER =
        Pattern.compile("^\\\\s*[a-zA-Z\\\\-]+\\\\s*[:]{1}\\\\s[a-zA-Z0-9\\\\s.#]+[;]{1}")

    //提取页面超链接,提取html中的超链接。
    private val HYPERLINK_PATTER =
        Pattern.compile("(<a\\\\s*(?!.*\\\\brel=)[^>]*)(href=\"https?:\\\\/\\\\/)((?!(?:(?:www\\\\.)?'.implode('|(?:www\\\\.)?', \$follow_list).'))[^\"]+)\"((?!.*\\\\brel=)[^>]*)(?:[^>]*)>")

    //提取网页图片,假若你想提取网页中所有图片信息，可以利用下面的表达式。
    private val IMAGE_PATTER =
        Pattern.compile("\\\\< *[img][^\\\\\\\\>]*[src] *= *[\\\\\"\\\\']{0,1}([^\\\\\"\\\\'\\\\ >]*)")

    //提取Color Hex Codes,有时需要抽取网页中的颜色代码，可以使用下面的表达式。
    private val COLOR_PATTER = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")

    //文件路径及扩展名校验,验证windows下文件路径和扩展名（下面的例子中为.txt文件）
    private val ROUTE_PATTER =
        Pattern.compile("^([a-zA-Z]\\\\:|\\\\\\\\)\\\\\\\\([^\\\\\\\\]+\\\\\\\\)*[^\\\\/:*?\"<>|]+\\\\.txt(l)?$")

    //提取URL链接,下面的这个表达式可以筛选出一段文本中的URL
    // ^(f|ht){1}(tp|tps):\\/\\/([\\w-]+\\.)+[\\w-]+(\\/[\\w- ./?%&=]*)?
    //检查URL的前缀,应用开发中很多时候需要区分请求是HTTPS还是HTTP，通过下面的表达式可以取出一个url的前缀然后再逻辑判断。
//if (!s.match(/^[a-zA-Z]+:\\/\\//))
//	{
//		s = 'http://' + s;
//	}
    //校验IP-v6地址
//	(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))
//校验IP-v4地址
//	\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b
//	判断IE的版本
//	^.*MSIE [5-8](?:\\.[0-9]+)?(?!.*Trident\\/[5-9]\\.0).*$
//	校验金额
//^[0-9]+(.[0-9]{2})?$
//	校验密码强度
//^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$

    /**
     * 验证是否为空串 (包括空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串,返回true)
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isNullString(str: String?): Boolean {
        if (str == null || "" == str || str.isEmpty()) {
            return true
        }
        for (element in str) {
            if (element != ' ' && element != '\t' && element != '\r' && element != '\n') {
                return false
            }
        }
        return true
    }


    /**
     * 是否不为空
     *
     * @param s
     */
    fun isNotEmpty(s: String?): Boolean {
        return s != null && "" != s.trim { it <= ' ' }
    }

    /**
     * 验证非零正整数
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isNotZero(str: String): Boolean {
        return NOTZERO_PATTERN.matcher(str).matches()
    }


    /**
     * 验证是数字
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isNumber(str: String): Boolean {
        return NUMBER_PATTERN.matcher(str).matches()
    }


    /**
     * 验证是大写字母
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isUpChar(str: String): Boolean {
        return UPCHAR_PATTERN.matcher(str).matches()
    }


    /**
     * 验证是小写字母
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isLowChar(str: String): Boolean {
        return LOWCHAR_PATTERN.matcher(str).matches()
    }


    /**
     * 验证是英文字母
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isLetter(str: String): Boolean {
        return LETTER_PATTERN.matcher(str).matches()
    }


    /**
     * 验证输入汉字
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isChinese(str: String): Boolean {
        return CHINESE_PATTERN.matcher(str).matches()
    }


    /**
     * 验证真实姓名
     *
     * @param str 验证字符
     * @return
     */
    fun isRealName(str: String): Boolean {
        return REALNEM_PATTERN.matcher(str).matches()
    }


    /**
     * 验证是否是条形码
     *
     * @param oneCode 条形码
     * @return boolean
     */
    fun isOneCode(oneCode: String): Boolean {
        return ONECODE_PATTERN.matcher(oneCode).matches()
    }


    /**
     * 是否含有特殊符号
     *
     * @param str 待验证的字符串
     * @return 是否含有特殊符号
     */
    fun hasSpecialCharacter(str: String): Boolean {
        val regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        return m.find()
    }


    /**
     * 验证邮箱是否正确
     *
     * @param email 邮箱地址
     * @return boolean
     */
    fun isEmail(email: String): Boolean {
        return EMAIL_PATTERN.matcher(email).matches()
    }


    /**
     * 验证手机号是否正确
     *
     * @param phone 手机号码
     * @return boolean
     */
    fun isPhone(phone: String): Boolean {
        return PHONE_PATTERN.matcher(phone).matches()
    }


    /**
     * 验证座机号码是否正确
     *
     * @param plane 座机号码
     * @return boolean
     */
    fun isPlane(plane: String): Boolean {
        return PLANE_PATTERN.matcher(plane).matches()
    }


    /**
     * 验证邮政编码是否正确
     *
     * @param postalcode 邮政编码
     * @return boolean
     */
    fun isPostalCode(postalcode: String): Boolean {
        return POSTALCODE_PATTERN.matcher(postalcode).matches()
    }


    /**
     * 验证IP地址是否正确
     *
     * @param ipaddress IP地址
     * @return boolean
     */
    fun isIpAddress(ipaddress: String): Boolean {
        return IPADDRESS_PATTERN.matcher(ipaddress).matches()
    }


    /**
     * 验证URL地址是否正确
     *
     * @param url 地址
     * @return boolean
     */
    fun isURL(url: String): Boolean {
        return URL_PATTERN.matcher(url).matches()
    }


    /**
     * 验证是否是正整数
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isInteger(str: String): Boolean {
        return try {
            Integer.valueOf(str)
            true
        } catch (e: Exception) {
            false
        }

    }


    /**
     * 验证是否是小数
     *
     * @param paramString 验证字符
     * @return boolean
     */
    fun isPoint(paramString: String): Boolean {
        if (paramString.indexOf(".") > 0) {
            if (paramString.substring(paramString.indexOf(".")).length > 3) {
                return false
            }
        }
        return true
    }


    /**
     * 验证是否银行卡号
     *
     * @param bankNo 银行卡号
     * @return
     */
    fun isBankNo(bankNo: String): Boolean {
        var bankNo = bankNo
        //替换空格
        bankNo = bankNo.replace(" ".toRegex(), "")
        //银行卡号可为12位数字
        return if (12 == bankNo.length) {
            true
        } else BANKNO_PATTERN.matcher(bankNo).matches()
        //银行卡号可为16-19位数字
    }

    /**
     * 验证身份证号码是否正确
     *
     * @param IDCardNo 身份证号码
     * @return boolean
     */
    fun isIDCard(IDCardNo: String): Boolean {
        //记录错误信息
        var errmsg = ""
        val ValCodeArr = arrayOf("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2")
        val Wi = arrayOf(
            "7",
            "9",
            "10",
            "5",
            "8",
            "4",
            "2",
            "1",
            "6",
            "3",
            "7",
            "9",
            "10",
            "5",
            "8",
            "4",
            "2"
        )
        var Ai = ""

        //================ 身份证号码的长度 15位或18位 ================
        if (IDCardNo.length != 15 && IDCardNo.length != 18) {
            errmsg = "身份证号码长度应该为15位或18位!"
            return false
        }

        //================ 数字 除最后以为都为数字 ================
        if (IDCardNo.length == 18) {
            Ai = IDCardNo.substring(0, 17)
        } else if (IDCardNo.length == 15) {
            Ai = IDCardNo.substring(0, 6) + "19" + IDCardNo.substring(6, 15)
        }
        if (!isNumber(Ai)) {
            errmsg = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字"
            return false
        }

        //================ 出生年月是否有效 ================
        //年份
        val strYear = Ai.substring(6, 10)
        //月份
        val strMonth = Ai.substring(10, 12)
        //日
        val strDay = Ai.substring(12, 14)
        if (!getDateIsTrue(strYear, strMonth, strDay)) {
            errmsg = "身份证生日无效"
            return false
        }
        val gc = GregorianCalendar()
        val s = SimpleDateFormat("yyyy-MM-dd")
        try {
            if (gc.get(Calendar.YEAR) - Integer.parseInt(strYear) > 150 || gc.time.time - s.parse("$strYear-$strMonth-$strDay").time < 0) {
                errmsg = "身份证生日不在有效范围"
                return false
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            errmsg = "身份证生日不在有效范围"
            return false
        } catch (e1: ParseException) {
            e1.printStackTrace()
            errmsg = "身份证生日不在有效范围"
            return false
        }

        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errmsg = "身份证月份无效"
            return false
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errmsg = "身份证日期无效"
            return false
        }

        //================ 地区码时候有效 ================
        val hashtable = getAreaCodeAll()
        if (hashtable[Ai.substring(0, 2)] == null) {
            errmsg = "身份证地区编码错误"
            return false
        }

        //================ 判断最后一位的值 ================
        var totalmulAiWi = 0
        for (i in 0..16) {
            totalmulAiWi += Integer.parseInt(Ai[i].toString()) * Integer.parseInt(Wi[i])
        }
        val modValue = totalmulAiWi % 11
        val strVerifyCode = ValCodeArr[modValue]
        Ai += strVerifyCode
        if (IDCardNo.length == 18) {
            if (!(Ai == IDCardNo)) {
                errmsg = "身份证无效，不是合法的身份证号码"
                return false
            }
        } else {
            return true
        }
        return true
    }

    /**
     * 检查日期是否有效
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return boolean
     */
    fun getDateIsTrue(year: String, month: String, day: String): Boolean {
        try {
            val data = year + month + day
            val simpledateformat = SimpleDateFormat("yyyyMMdd")
            simpledateformat.isLenient = false
            simpledateformat.parse(data)
        } catch (e: ParseException) {
            e.printStackTrace()
            return false
        }

        return true
    }

    /**
     * 获取身份证号所有区域编码设置
     *
     * @return Hashtable
     */
    fun getAreaCodeAll(): Hashtable<*, *> {
        val hashtable = Hashtable<String, String>()
        hashtable.put("11", "北京")
        hashtable.put("12", "天津")
        hashtable.put("13", "河北")
        hashtable.put("14", "山西")
        hashtable.put("15", "内蒙古")
        hashtable.put("21", "辽宁")
        hashtable.put("22", "吉林")
        hashtable.put("23", "黑龙江")
        hashtable.put("31", "上海")
        hashtable.put("32", "江苏")
        hashtable.put("33", "浙江")
        hashtable.put("34", "安徽")
        hashtable.put("35", "福建")
        hashtable.put("36", "江西")
        hashtable.put("37", "山东")
        hashtable.put("41", "河南")
        hashtable.put("42", "湖北")
        hashtable.put("43", "湖南")
        hashtable.put("44", "广东")
        hashtable.put("45", "广西")
        hashtable.put("46", "海南")
        hashtable.put("50", "重庆")
        hashtable.put("51", "四川")
        hashtable.put("52", "贵州")
        hashtable.put("53", "云南")
        hashtable.put("54", "西藏")
        hashtable.put("61", "陕西")
        hashtable.put("62", "甘肃")
        hashtable.put("63", "青海")
        hashtable.put("64", "宁夏")
        hashtable.put("65", "新疆")
        hashtable.put("71", "台湾")
        hashtable.put("81", "香港")
        hashtable.put("82", "澳门")
        hashtable.put("91", "国外")
        return hashtable
    }


    /**
     * 判断是否有特殊字符
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isPeculiarStr(str: String): Boolean {
        var flag = false
        val regEx = "[^0-9a-zA-Z\u4e00-\u9fa5]+"
        if (str.length != str.replace(regEx.toRegex(), "").length) {
            flag = true
        }
        return flag
    }


    /**
     * 判断是否为用户名账号(规则如下：用户名由下划线或字母开头，由数字、字母、下划线、点、减号组成的4-32位字符)
     *
     * @param username 用户名
     * @return boolean
     */
    fun isUserName(username: String): Boolean {
        return USERNAME_PATTERN.matcher(username).matches()
    }

    /**
     * 获取字符串中文字符的长度（每个中文算2个字符）.
     *
     * @param str 指定的字符串
     * @return 中文字符的长度
     */
    fun chineseLength(str: String): Int {
        var valueLength = 0
        val chinese = "[\u0391-\uFFE5]"
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        if (!isNullString(str)) {
            for (i in str.indices) {
                /* 获取一个字符 */
                val temp = str.substring(i, i + 1)
                /* 判断是否为中文字符 */
                if (temp.matches(chinese.toRegex())) {
                    valueLength += 2
                }
            }
        }
        return valueLength
    }

    /**
     * 描述：获取字符串的长度.
     *
     * @param str 指定的字符串
     * @return 字符串的长度（中文字符计2个）
     */
    fun strLength(str: String): Int {
        var valueLength = 0
        val chinese = "[\u0391-\uFFE5]"
        if (!isNullString(str)) {
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (i in str.indices) {
                // 获取一个字符
                val temp = str.substring(i, i + 1)
                // 判断是否为中文字符
                if (temp.matches(chinese.toRegex())) {
                    // 中文字符长度为2
                    valueLength += 2
                } else {
                    // 其他字符长度为1
                    valueLength += 1
                }
            }
        }
        return valueLength
    }

    /**
     * 描述：获取指定长度的字符所在位置.
     *
     * @param str  指定的字符串
     * @param maxL 要取到的长度（字符长度，中文字符计2个）
     * @return 字符的所在位置
     */
    fun subStringLength(str: String, maxL: Int): Int {
        var currentIndex = 0
        var valueLength = 0
        val chinese = "[\u0391-\uFFE5]"
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (i in str.indices) {
            // 获取一个字符
            val temp = str.substring(i, i + 1)
            // 判断是否为中文字符
            if (temp.matches(chinese.toRegex())) {
                // 中文字符长度为2
                valueLength += 2
            } else {
                // 其他字符长度为1
                valueLength += 1
            }
            if (valueLength >= maxL) {
                currentIndex = i
                break
            }
        }
        return currentIndex
    }

    /**
     * 描述：是否只是字母和数字.
     *
     * @param str 指定的字符串
     * @return 是否只是字母和数字:是为true，否则false
     */
    fun isNumberLetter(str: String): Boolean? {
        var isNoLetter: Boolean? = false
        val expr = "^[A-Za-z0-9]+$"
        if (str.matches(expr.toRegex())) {
            isNoLetter = true
        }
        return isNoLetter
    }

    /**
     * 描述：是否包含中文.
     *
     * @param str 指定的字符串
     * @return 是否包含中文:是为true，否则false
     */
    fun isContainChinese(str: String): Boolean? {
        var isChinese: Boolean? = false
        val chinese = "[\u0391-\uFFE5]"
        if (!isNullString(str)) {
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (i in str.indices) {
                // 获取一个字符
                val temp = str.substring(i, i + 1)
                // 判断是否为中文字符
                if (temp.matches(chinese.toRegex())) {
                    isChinese = true
                } else {

                }
            }
        }
        return isChinese
    }

    /**
     * 描述：从输入流中获得String.
     *
     * @param is 输入流
     * @return 获得的String
     */
    fun convertStreamToString(`is`: InputStream): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String?
        try {
            line = reader.readLine()
            while (line != null) {
                sb.append(line + "\n")
                line = reader.readLine()
            }

            // 最后一个\n删除
            if (sb.indexOf("\n") != -1 && sb.lastIndexOf("\n") == sb.length - 1) {
                sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n") + 1)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return sb.toString()
    }

    /**
     * 描述：截取字符串到指定字节长度.
     *
     * @param str    the str
     * @param length 指定字节长度
     * @return 截取后的字符串
     */
    fun cutString(str: String, length: Int): String {
        return cutString(str, length, "")
    }

    /**
     * 描述：截取字符串到指定字节长度.
     *
     * @param str    文本
     * @param length 字节长度
     * @param dot    省略符号
     * @return 截取后的字符串
     */
    fun cutString(str: String, length: Int, dot: String?): String {
        val strBLen = strlen(str, "GBK")
        if (strBLen <= length) {
            return str
        }
        var temp = 0
        val sb = StringBuffer(length)
        val ch = str.toCharArray()
        for (c in ch) {
            sb.append(c)
            if (c.toInt() > 256) {
                temp += 2
            } else {
                temp += 1
            }
            if (temp >= length) {
                if (dot != null) {
                    sb.append(dot)
                }
                break
            }
        }
        return sb.toString()
    }

    /**
     * 描述：截取字符串从第一个指定字符.
     *
     * @param str1   原文本
     * @param str2   指定字符
     * @param offset 偏移的索引
     * @return 截取后的字符串
     */
    fun cutStringFromChar(str1: String, str2: String, offset: Int): String {
        if (isNullString(str1)) {
            return ""
        }
        val start = str1.indexOf(str2)
        if (start != -1) {
            if (str1.length > start + offset) {
                return str1.substring(start + offset)
            }
        }
        return ""
    }

    /**
     * 描述：获取字节长度.
     *
     * @param str     文本
     * @param charset 字符集（GBK）
     * @return the int
     */
    fun strlen(str: String?, charset: String): Int {
        if (str == null || str.isEmpty()) {
            return 0
        }
        var length = 0
        try {
            length = str.toByteArray(charset(charset)).size
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return length
    }

    /**
     * 获取大小的描述.
     *
     * @param size 字节个数
     * @return 大小的描述
     */
    fun getSizeDesc(size: Long): String {
        var size = size
        var suffix = "B"
        if (size >= 1024) {
            suffix = "K"
            size = size shr 10
            if (size >= 1024) {
                suffix = "M"
                // size /= 1024;
                size = size shr 10
                if (size >= 1024) {
                    suffix = "G"
                    size = size shr 10
                    // size /= 1024;
                }
            }
        }
        return size.toString() + suffix
    }

    /**
     * 描述：ip地址转换为10进制数.
     *
     * @param ip the ip
     * @return the long
     */
    fun ip2int(ip: String): Long {
        var ip = ip
        ip = ip.replace(".", ",")
        val items = ip.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return (java.lang.Long.valueOf(items[0]) shl 24 or (java.lang.Long.valueOf(items[1]) shl 16)
                or (java.lang.Long.valueOf(items[2]) shl 8) or java.lang.Long.valueOf(items[3]))
    }

    /**
     * 获取UUID
     *
     * @return 32UUID小写字符串
     */
    fun gainUUID(): String {
        var strUUID = UUID.randomUUID().toString()
        strUUID = strUUID.replace("-".toRegex(), "").toLowerCase()
        return strUUID
    }


    /**
     * 手机号码，中间4位星号替换
     *
     * @param phone 手机号
     * @return 星号替换的手机号
     */
    fun phoneNoHide(phone: String): String {
        // 括号表示组，被替换的部分$n表示第n组的内容
        // 正则表达式中，替换字符串，括号的意思是分组，在replace()方法中，
        // 参数二中可以使用$n(n为数字)来依次引用模式串中用括号定义的字串。
        // "(\d{3})\d{4}(\d{4})", "$1****$2"的这个意思就是用括号，
        // 分为(前3个数字)中间4个数字(最后4个数字)替换为(第一组数值，保持不变$1)(中间为*)(第二组数值，保持不变$2)
        return phone.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
    }

    /**
     * 银行卡号，保留最后4位，其他星号替换
     *
     * @param cardId 卡号
     * @return 星号替换的银行卡号
     */
    fun cardIdHide(cardId: String): String {
        return cardId.replace("\\d{15}(\\d{3})".toRegex(), "**** **** **** **** $1")
    }

    /**
     * 身份证号，中间10位星号替换
     *
     * @param id 身份证号
     * @return 星号替换的身份证号
     */
    fun idHide(id: String): String {
        return id.replace("(\\d{4})\\d{10}(\\d{4})".toRegex(), "$1** **** ****$2")
    }

    /**
     * 是否为车牌号（沪A88888）
     *
     * @param vehicleNo 车牌号
     * @return 是否为车牌号
     */

    fun checkVehicleNo(vehicleNo: String): Boolean {
        val pattern = Pattern.compile("^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{5}$")
        return pattern.matcher(vehicleNo).find()

    }

//	/**
//	 * 匹配中国邮政编码
//	 *
//	 * @param postcode 邮政编码
//	 * @return 验证成功返回true，验证失败返回false
//	 */
//	public static boolean checkPostcode(String postcode) {
//		String regex = "[1-9]\\d{5}";
//		return Pattern.matches(regex, postcode);
//	}


    /**
     * 判断字符串是否为连续数字 45678901等
     *
     * @param str 待验证的字符串
     * @return 是否为连续数字
     */
    fun isContinuousNum(str: String): Boolean {
        if (TextUtils.isEmpty(str))
            return false
        if (!isNumber(str))
            return true
        val len = str.length
        for (i in 0 until len - 1) {
            val curChar = str[i]
            var verifyChar = (curChar.toInt() + 1).toChar()
            if (curChar == '9')
                verifyChar = '0'
            val nextChar = str[i + 1]
            if (nextChar != verifyChar) {
                return false
            }
        }
        return true
    }

    /**
     * 是否是纯字母
     *
     * @param str 待验证的字符串
     * @return 是否是纯字母
     */
    fun isAlphaBetaString(str: String): Boolean {
        if (TextUtils.isEmpty(str)) {
            return false
        }

        val p = Pattern.compile("^[a-zA-Z]+$")// 从开头到结尾必须全部为字母或者数字
        val m = p.matcher(str)

        return m.find()
    }

    /**
     * 判断字符串是否为连续字母 xyZaBcd等
     *
     * @param str 待验证的字符串
     * @return 是否为连续字母
     */
    fun isContinuousWord(str: String): Boolean {
        if (TextUtils.isEmpty(str))
            return false
        if (!isAlphaBetaString(str))
            return true
        val len = str.length
        val local = str.toLowerCase()
        for (i in 0 until len - 1) {
            val curChar = local[i]
            var verifyChar = (curChar.toInt() + 1).toChar()
            if (curChar == 'z')
                verifyChar = 'a'
            val nextChar = local[i + 1]
            if (nextChar != verifyChar) {
                return false
            }
        }
        return true
    }

    /**
     * 是否是日期
     * 20120506 共八位，前四位-年，中间两位-月，最后两位-日
     *
     * @param date    待验证的字符串
     * @param yearlen yearlength
     * @return 是否是真实的日期
     */
    fun isRealDate(date: String?, yearlen: Int): Boolean {
        val len = 4 + yearlen
        if (date == null || date.length != len)
            return false

        if (!date.matches("[0-9]+".toRegex()))
            return false

        val year = Integer.parseInt(date.substring(0, yearlen))
        val month = Integer.parseInt(date.substring(yearlen, yearlen + 2))
        val day = Integer.parseInt(date.substring(yearlen + 2, yearlen + 4))

        if (year <= 0)
            return false
        if (month <= 0 || month > 12)
            return false
        if (day <= 0 || day > 31)
            return false

        when (month) {
            4, 6, 9, 11 -> return day <= 30
            2 -> {
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                    return day <= 29
                return day <= 28
            }
            else -> return true
        }
    }
}