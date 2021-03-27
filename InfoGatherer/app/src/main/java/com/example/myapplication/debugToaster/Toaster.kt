package com.example.myapplication.debugToaster

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import java.util.*

/**
 * Default Long toast duration
 */
private const val LongToastDuration = 3500

/**
 * Default Short toast duration
 */
private const val ShortToastDuration = 2000

class Toaster private constructor(activity: Activity) {

    var activity : Activity? = activity

    var duration = Toast.LENGTH_LONG

    var copyGenericInfoBuilder : CopyToClipboardGenericInfoBuilder? = null

    private var toastQueue : LinkedList<ToastDataHolder> = LinkedList()
    private var isShowing = false

    companion object {

        private var instance : Toaster? = null

        @JvmStatic
        fun show(@NonNull activity : Activity, @Nullable copyGenericInfoBuilder : CopyToClipboardGenericInfoBuilder? = null, duration : Int = 1) : Toaster {
            if (instance != null) {
                instance?.activity = activity
            } else {
                instance = Toaster(activity)
            }
            return show(copyGenericInfoBuilder, duration)!!
        }

        @JvmStatic
        fun show(@Nullable copyGenericInfoBuilder : CopyToClipboardGenericInfoBuilder? = null, duration : Int = 1) : Toaster? {
            instance?.duration = when (duration) {
                Toast.LENGTH_LONG -> LongToastDuration
                Toast.LENGTH_SHORT -> ShortToastDuration
                else -> duration
            }

            copyGenericInfoBuilder?.let {
                instance?.copyGenericInfoBuilder = it
            }
            return instance
        }
    }

    //----------------------------- Controls -----------------------------

    fun success(msg : String, @Nullable extraInfo : String? = null) = add(ToastDataHolder(msg, duration.toLong(), ToastType.Success, extraInfo))

    fun error(msg : String, @Nullable extraInfo : String? = null) = add(ToastDataHolder(msg, duration.toLong(), ToastType.Error, extraInfo))

    fun warning(msg : String, @Nullable extraInfo : String? = null) = add(ToastDataHolder(msg, duration.toLong(), ToastType.Warning, extraInfo))

    fun debug(msg : String, @Nullable extraInfo : String? = null) = add(ToastDataHolder(msg, duration.toLong(), ToastType.Debug, extraInfo))

    fun clearQueue() = toastQueue.clear()

    //--------------------------- End of Controls ------------------------

    /**
     * Add Toast information to the queue
     *
     * @param dataHolder Toast information
     */
    private fun add(dataHolder: ToastDataHolder) {
        toastQueue.add(buildExtraInfo(dataHolder))
        if (!isShowing) {
            showFirst()
        }
    }

    /**
     * Show toast on TOP of the queue
     */
    private fun showFirst() {
        if (!toastQueue.isEmpty()) {
            isShowing = true
            val dataHolder = toastQueue.first
            toastQueue.removeFirst()

            show(dataHolder)

            Handler(Looper.getMainLooper()).postDelayed({ showFirst() }, 3000)
        } else {
            isShowing = false
        }
    }

    private fun show(dataHolder: ToastDataHolder) {
        activity?.let { DebugToast.show(it, dataHolder) }
    }

    /**
     * Build extra info. Assess if there is extra info to be copied. If so, append GenericInfo...
     *
     * @param dataHolder    Data Holder that may contain extra info
     * @return  ToastDataHolder with either no extra info, or with extra info + generic info
     */
    private fun buildExtraInfo(dataHolder: ToastDataHolder) : ToastDataHolder {
        dataHolder.extraInfo?.let {
            dataHolder.extraInfo = "General Information: ${copyGenericInfoBuilder?.buildGenericInfo()} Specific Information: $it"
        }
        return dataHolder
    }

    /**
     * Implement this to build the Generic information that the user can copy to clipboard
     * @constructor Create empty Copy to clipboard generic info builder
     */
    interface CopyToClipboardGenericInfoBuilder { fun buildGenericInfo() : String }

}
