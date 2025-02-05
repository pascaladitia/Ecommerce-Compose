package com.pascal.ecommercecompose.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("dd_MM_yyyy_hhmmssSSS", Locale.getDefault()).format(Date())
    val fileName = "Foto_${timeStamp}.jpg"
    val image = File.createTempFile(
        fileName,
        ".jpg",
        context.externalCacheDir
    )
    return image
}

fun Bitmap.getImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("dd_MM_yyyy_hhmmssSSS", Locale.getDefault()).format(Date())
    val fileName = "Fie_${timeStamp}.jpg"

    val wrapper = ContextWrapper(context)
    var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
    file = File(file, fileName)
    val stream: OutputStream = FileOutputStream(file)
    this.compress(Bitmap.CompressFormat.JPEG,25,stream)
    stream.flush()
    stream.close()
    return file
}

fun File.toBitmap(): Bitmap {
    val fis = FileInputStream(this)
    return BitmapFactory.decodeStream(fis)
}

fun base64ToBitMap(encodedString: String?): Bitmap? {
    return try {
        val encodeByte =
            Base64.decode(encodedString, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    } catch (e: java.lang.Exception) {
        e.message
        null
    }
}

fun urlToBitmap(imageUrl: String): Bitmap? {
    return try {
        val url = URL(imageUrl)
        val inputStream = url.openStream()
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getFilePath(context: Context): File? {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        }

        else -> {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "jari"
            )
        }
    }
}

fun uriFromFile(context: Context, file: File): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    } else {
        Uri.fromFile(file)
    }
}

@Throws(IOException::class)
private fun Uri.getFileExtension(contentResolver: ContentResolver): String {
    var extension: String? = null

    if (ContentResolver.SCHEME_CONTENT == scheme && contentResolver.getType(this)
            ?.startsWith("image/") == true
    ) {
        val cursor: Cursor? = contentResolver.query(
            this,
            arrayOf(MediaStore.Images.ImageColumns.MIME_TYPE),
            null,
            null,
            null
        )
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE)
                if (columnIndex != -1) {
                    val mimeType = it.getString(columnIndex)
                    extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                }
            }
        }
    }

    if (extension.isNullOrEmpty()) {
        extension = MimeTypeMap.getFileExtensionFromUrl(toString())
    }

    if (extension.isNullOrEmpty()) {
        extension = "jpg"
    }

    return extension!!
}

fun bitmapToBase64(bitmap: Bitmap?): String {
    return if (bitmap != null) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT)
    } else { "" }
}