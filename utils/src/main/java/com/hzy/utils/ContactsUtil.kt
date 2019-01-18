package com.hzy.utils

import android.content.ContentProvider
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import androidx.annotation.RequiresPermission
import com.hzy.utils.bean.Contacts
import java.util.jar.Manifest


/**
 * 联系人相关工具类
 * @author: ziye_huang
 * @date: 2019/1/17
 */
object ContactsUtil {

    /**
     * 获取手机联系人信息
     */
    @RequiresPermission(android.Manifest.permission.READ_CONTACTS)
    fun getContacts(context: Context): List<Contacts> {
        val contactsList = mutableListOf<Contacts>()
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            cursor?.let {
                while (cursor.moveToNext()) {
                    val contactsName =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phoneNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    contactsList.add(Contacts(contactsName, phoneNumber))
                }
                return contactsList
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return listOf()
    }

}