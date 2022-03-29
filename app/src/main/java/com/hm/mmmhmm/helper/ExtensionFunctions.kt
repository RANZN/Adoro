package com.hm.mmmhmm.helper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hm.mmmhmm.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.AssertionError
import java.lang.Error
import java.net.URLEncoder


//------------------------/ TOAST /---------------------------------
fun Context?.toast(message: String?, time: Int = Toast.LENGTH_SHORT) {
    this?.let {
        Toast.makeText(it, message, time).show()
    }
}

fun Fragment?.toast(message: Int, time: Int = Toast.LENGTH_SHORT) {
    this?.activity?.let {
        Toast.makeText(it, message, time).show()
    }
}


//    --------------------------/ GLIDE IMAGE VIEW /-----------------------------
fun ImageView.load(
    url: String?,
    placeholder: Int = R.color.text_gray,
    error: Int = R.color.text_gray,
    isCircleCrop: Boolean = false
) {
    if (this.context.applicationContext != null) {
        val glide = Glide.with(this.context.applicationContext).load(url).thumbnail(0.1F)
        if (placeholder != -1)
            glide.apply(RequestOptions().placeholder(placeholder).error(placeholder))
        if (isCircleCrop)
            glide.apply(RequestOptions.circleCropTransform())
        glide.into(this)
    }

}

fun ImageView.load(
    res: Int?,
    placeholder: Int = R.color.text_gray,
    isCircleCrop: Boolean = false
) {
    val glide = Glide.with(this.context).load(res).thumbnail(0.1F)
    if (placeholder != -1)
        glide.apply(RequestOptions().placeholder(placeholder).error(placeholder))
    if (isCircleCrop)
        glide.apply(RequestOptions.circleCropTransform())
    glide.into(this)
}

fun String.toRequestBody(): RequestBody {
    return RequestBody.create(
        MediaType.parse("text/plain"),
        this
    ) as RequestBody

}



fun File.toMultipartBody(
    fieldName: String,
    parseType: String = "multipart/form-data",
    fileName: String = URLEncoder.encode(this.name, "utf-8")
): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        fieldName, fileName, (RequestBody.create(MediaType.parse(parseType), this))
    )
}

fun Context.copyToClipboard(text: CharSequence){
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    clipboard?.setPrimaryClip(ClipData.newPlainText("",text))
}


