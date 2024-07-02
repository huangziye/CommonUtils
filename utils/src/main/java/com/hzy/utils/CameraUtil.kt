package com.hzy.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.FileProvider
import java.io.File


/**
 * Camera 相关工具类
 * @author: ziye_huang
 * @date: 2019/1/21
 */
object CameraUtil {
    /**
     * 拍照
     */
    const val TAKE_PHOTO = 1

    /**
     * 选择照片
     */
    const val CHOOSE_PHOTO = 2
    private lateinit var imageUri: Uri

    //拍照后的文件
    private lateinit var outputFile: File

    /**
     * 调用系统相机拍照
     */
    @RequiresPermission("android.permission.CAMERA")
    fun takePhoto(context: Context, packageName: String, outputImageName: String) {
        outputFile = File(context.externalCacheDir, outputImageName)
        if (outputFile.exists()) outputFile.delete()
        outputFile.createNewFile()
        imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            FileProvider.getUriForFile(context, "$packageName.fileprovider", outputFile)
        else Uri.fromFile(outputFile)
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        if (context is Activity) {
            context.startActivityForResult(intent, TAKE_PHOTO)
        }
    }

    /**
     * 获取拍摄后的照片
     */
    fun getTakePhoto(context: Context): Bitmap {
        return BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUri))
    }

    /**
     * 获取拍摄的图片路径
     */
    fun getTakePhotoPath(): String {
        return outputFile.absolutePath
    }

    /**
     * 选择照片
     */
    @RequiresPermission("android.permission.WRITE_EXTERNAL_STORAGE")
    fun choosePhoto(context: Context) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        if (context is Activity) {
            context.startActivityForResult(intent, CHOOSE_PHOTO)
        }
    }

    /**
     * 获取选择照片的路径
     */
    fun getChoosePhoto(context: Context, intent: Intent): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4及以上
            handleImageOnKitkat(context, intent)
        } else {
            //4.4以下
            handleImageBeforeKitkat(context, intent)
        }
    }

    /**
     * 得到正确路径
     *
     *
     * 值得注意的是在Android4.4以前可以通过data.getpath()来直接得到文件路径，
     * 但是4.4以后通过这个方法得到是路径不是正确路径（这里说的是图片）所以我
     * 们用下面getPath（）方法来解决这个问题。另外在6.0版本以后读写文件要有运行时权限
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT//sdk版本是否大于4.4
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return "${Environment.getExternalStorageDirectory()}/$split[1]"
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }


    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * 4.4及以上版本使用这种方式获取图片路径
     */
    @TargetApi(19)
    private fun handleImageOnKitkat(context: Context, intent: Intent): String? {
        var imagePath: String? = null
        val uri = intent.data
        if (null != uri) {
            when {
                DocumentsContract.isDocumentUri(context, uri) -> {
                    //如果是 document 类型的uri，则通过document id 处理
                    val docId = DocumentsContract.getDocumentId(uri)
                    if ("com.android.providers.media.documents" == uri.authority) {
                        //解析出数字格式的id
                        val id = docId.split(":")[1]
                        val selection = MediaStore.Images.Media._ID + "=" + id
                        imagePath = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
                    } else if ("com.android.providers.downloads.documents" == uri.authority) {
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            docId.toLong()
                        )
                        imagePath = getImagePath(context, contentUri, null)
                    }
                }
                "content".equals(uri.scheme, true) -> {
                    // 如果是 content 类型的 uri，则使用普通方式处理
                    imagePath = getImagePath(context, uri, null)
                }
                "file".equals(uri.scheme, true) -> {
                    //如果是file 类型的uri，直接获取图片路径即可
                    imagePath = uri.path
                }
            }
        }
        return imagePath
    }

    /**
     * 4.4 版本以下使用这种方式获取图片路径
     */
    private fun handleImageBeforeKitkat(context: Context, intent: Intent): String? {
        val uri = intent.data
        if (null != uri) {
            return getImagePath(context, intent.data!!, null)
        }
        return null
    }

    private fun getImagePath(context: Context, uri: Uri, selection: String?): String? {
        var path: String? = null
        //通过uri和selection 来获取真实的图片路径
        val cursor = context.contentResolver.query(uri, null, selection, null, null)
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }
}