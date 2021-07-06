package com.spartancookie.hermeslogger.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.models.LogDataHolder

private const val CopyDefaultLabel = "Clipboard info"

const val EMPTY_STRING = ""
const val NO_RES = 0
const val DateFormat = "dd/MM 'at' HH:mm:ss.SSS"

/**
 * Copy info from [dataHolder] to the clipboard
 * @param activity      Activity reference
 * @param dataHolder    Instance of [LogDataHolder]
 */
internal fun copyToClipboard(activity: Activity, dataHolder: LogDataHolder) {
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
internal fun buildInfo(dataHolder: LogDataHolder) = buildString {
    append("${dataHolder.creationDate} - ${dataHolder.type} Message: ${dataHolder.message} ")
    dataHolder.genericInfo?.let { append("System info: $it ") }
    dataHolder.extraInfo?.let { append("Extra information: $it.") }
}

/**
 * Create a string containing the information on the log data holder
 * @param dataHolder Log data holder
 * @return  String that contains information from the log data holder received
 */
internal fun buildInfoContentOnly(dataHolder: LogDataHolder) = buildString {
    dataHolder.extraInfo?.let { append("Extra information: $it.") }
    dataHolder.genericInfo?.let { append("System info: $it ") }
}

/**
 * Directly set a default type onto a [MutableLiveData] instance
 */
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

internal fun removeFromStack(fragmentManager: FragmentManager, fragmentTag: String) {
    fragmentManager.run {
        val fragment = findFragmentByTag(fragmentTag)
        if (fragment != null) {
            val transaction = beginTransaction()
            transaction.remove(fragment)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            transaction.commit()
        }
    }
}