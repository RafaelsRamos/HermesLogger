package com.example.myapplication.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.models.LogDataHolder

private const val CopyDefaultLabel = "Clipboard info"

const val DateFormat = "dd/MM 'at' HH:mm:ss.SSS"

/**
 * Copy info from [dataHolder] to the clipboard
 * @param activity      Activity reference
 * @param dataHolder    Instance of [LogDataHolder]
 */
fun copyToClipboard(activity: Activity, dataHolder: LogDataHolder) {
    val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val info = buildInfo(dataHolder)
    val clip = ClipData.newPlainText(CopyDefaultLabel, info)
    clipboard?.setPrimaryClip(clip)
}

fun buildInfo(dataHolder: LogDataHolder) = buildString {
        append("${dataHolder.creationDate} - ${dataHolder.type} Message: ${dataHolder.msg} ")
        dataHolder.genericInfo?.let { append("Generic information: $it ") }
        dataHolder.extraInfo?.let { append("Extra information: $it.") }
    }

/**
 * Directly set a default type onto a [MutableLiveData] instance
 */
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }