package com.adnan.e_book_adda.util

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.File

object FileUtils {

    fun getPdfNameFromAssets(): String {
        return "MindOrks_Android_Online_Professional_Course-Syllabus.pdf"
    }

    fun getPdfUrl(): String {
        return "https://ebookadda.s3.amazonaws.com/maths.pdf"}

    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

}


