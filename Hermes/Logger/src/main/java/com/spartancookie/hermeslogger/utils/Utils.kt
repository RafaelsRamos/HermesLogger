package com.spartancookie.hermeslogger.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.spartancookie.hermeslogger.models.EventDataHolder
import java.io.File
import java.io.FileReader
import java.io.FileWriter

private const val CopyDefaultLabel = "Clipboard info"

internal const val EMPTY_STRING = ""
internal const val NO_RES = 0
internal const val DateFormat = "dd/MM 'at' HH:mm:ss.SSS"

/**
 * Copy info from [dataHolder] to the clipboard
 * @param activity      Activity reference
 * @param dataHolder    Instance of [EventDataHolder]
 */
internal fun copyToClipboard(activity: Activity, dataHolder: EventDataHolder) {
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
internal fun buildInfo(dataHolder: EventDataHolder) = buildString {
    append("${dataHolder.creationDate} - ${dataHolder.type} Message: ${dataHolder.message} ")
    dataHolder.extraInfo?.let { append("Extra information: $it.") }
    dataHolder.throwable?.let { append("Throwable:\n${it.stackTraceToString()}\n") }
    dataHolder.genericInfo?.let { append("System info: $it ") }
}

/**
 * Create a string containing the information on the log data holder
 * @param dataHolder Log data holder
 * @return  String that contains information from the log data holder received
 */
internal fun buildInfoContentOnly(dataHolder: EventDataHolder) = buildString {
    dataHolder.extraInfo?.let { append("Extra information: $it.\n") }
    dataHolder.throwable?.let { append("Throwable:\n${it.stackTraceToString()}\n") }
    dataHolder.genericInfo?.let { append("System info: $it\n") }
}

/**
 * Directly set a default type onto a [MutableLiveData] instance
 */
internal fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

internal fun removeFromStack(fragmentManager: FragmentManager, fragmentTag: String) {
    fragmentManager.run {
        val fragment = findFragmentByTag(fragmentTag)
        if (fragment != null) {
            val transaction = beginTransaction()
            transaction.remove(fragment)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            transaction.commitAllowingStateLoss()
        }
    }
}

/**
 * Create a temp file that is a copy from [referenceFile]
 * @param context Context
 * @param referenceFile File that will be copied
 */
internal fun createTempFileFromFile(context: Context, fileName: String, referenceFile: File): File {

    val tempFile = File.createTempFile(fileName, ".txt", context.externalCacheDir)

    val fw = FileWriter(tempFile)

    val fr = FileReader(referenceFile)
    var c: Int = fr.read()
    while (c != -1) {
        fw.write(c)
        c = fr.read()
    }
    fr.close()

    fw.flush()
    fw.close()

    return tempFile
}

/**
 * Clear all fragments in the @receiver
 */
internal fun FragmentManager.clearAllFragments() {
    for (fragment in fragments) {
        beginTransaction().remove(fragment).commit()
    }
}