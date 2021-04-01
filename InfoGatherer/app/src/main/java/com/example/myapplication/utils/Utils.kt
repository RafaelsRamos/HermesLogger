package com.example.myapplication.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.models.InfoDataHolder

private const val CopyDefaultLabel = "Clipboard info"

const val DateFormat = "dd/MM 'at' HH:mm:ss.SSS"

/**
 * Copy info from [dataHolder] to the clipboard
 * @param activity      Activity reference
 * @param dataHolder    Instance of [InfoDataHolder]
 */
fun copyToClipboard(activity: Activity, dataHolder: InfoDataHolder) {
    val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText(CopyDefaultLabel, dataHolder.extraInfo)
    clipboard?.setPrimaryClip(clip)
}

fun getInfoIcon(context: Context, position: Int): Drawable? {
    return context.let {
        ContextCompat.getDrawable(
            it,
            when (position) {
                0 -> R.drawable.ic_error
                1 -> R.drawable.ic_warning
                2 -> R.drawable.ic_debug
                3 -> R.drawable.ic_success
                else -> R.drawable.ic_unknown
            }
        )
    }
}

/**
 * Directly set a default type onto a [MutableLiveData] instance
 */
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }