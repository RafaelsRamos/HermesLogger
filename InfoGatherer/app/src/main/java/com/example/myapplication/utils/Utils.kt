package com.example.myapplication.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.models.LogDataHolder

private const val CopyDefaultLabel = "Clipboard info"

const val NO_RES = -1

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

/**
 * Create a string containing the information on the log data holder
 * @param dataHolder Log data holder
 * @return  String that contains information from the log data holder received
 */
fun buildInfo(dataHolder: LogDataHolder) = buildString {
        append("${dataHolder.creationDate} - ${dataHolder.type} Message: ${dataHolder.message} ")
        dataHolder.genericInfo?.let { append("Generic information: $it ") }
        dataHolder.extraInfo?.let { append("Extra information: $it.") }
    }

/**
 * Directly set a default type onto a [MutableLiveData] instance
 */
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }