package com.example.myapplication.debugToaster

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.myapplication.data.InfoHolder
import com.example.myapplication.models.LogDataHolder
import java.lang.ref.WeakReference
import java.util.*

private const val TAG = "Toaster"

/**
 * Default Long toast duration
 */
const val LongToastDuration = 3500
/**
 * Default Short toast duration
 */
const val ShortToastDuration = 2000

class Toaster {

    val infoHolder = InfoHolder()

    lateinit var actReference: WeakReference<Activity>

    var copyGenericInfoBuilder : CopyToClipboardGenericInfoBuilder? = null

    // List of logs that will be displayed as toasts
    private var logQueue : LinkedList<LogDataHolder> = LinkedList()
    private var isShowing = false

    private val activity get() = if (this::actReference.isInitialized) actReference.get() else null

    //----------------------------- Controls -----------------------------

    companion object {

        var instance = Toaster()

        fun success() = Builder().apply { type = LogType.Success }

        fun error() = Builder().apply { type = LogType.Error }

        fun warning() = Builder().apply { type = LogType.Warning }

        fun debug() = Builder().apply { type = LogType.Debug }

        fun info() = Builder().apply { type = LogType.Info }
    }

    fun clearQueue() = logQueue.clear()

    //--------------------------- Helper methods ------------------------

    /**
     * Add Toast information to the queue
     *
     * @param dataHolder Toast information
     */
    private fun add(dataHolder: LogDataHolder) {
        // Add log to the list of logs
        infoHolder.addInfo(dataHolder)

        // If we have a valid instance of the activity, show the toast
        activity?.run {
            logQueue.add(buildGenericInfo(dataHolder))
            if (!isShowing) {
                showFirst()
            }
        } ?: onNoActivityReference()
    }

    private fun showFirst() {
        if (!logQueue.isEmpty()) {
            isShowing = true
            val dataHolder = logQueue.first
            logQueue.removeFirst()

            showToast(dataHolder)

            Handler(Looper.getMainLooper()).postDelayed({ showFirst() }, dataHolder.duration)
        } else {
            isShowing = false
        }
    }

    private fun showToast(dataHolder: LogDataHolder) = activity?.run { DebugToast.show(this, dataHolder) }

    /**
     * Build extra info. Assess if there is extra info to be copied. If so, append GenericInfo...
     *
     * @param dataHolder    Data Holder that may contain extra info
     * @return  ToastDataHolder with either no extra info, or with extra info + generic info
     */
    private fun buildGenericInfo(dataHolder: LogDataHolder) : LogDataHolder {
        return dataHolder.apply {
            genericInfo = copyGenericInfoBuilder?.buildGenericInfo()
        }
    }

    private fun onNoActivityReference() {
        clearQueue()
        Log.w(TAG, "There is no valid instance of an activity. No toast will be shown.")
    }

    /**
     * Implement this to build the Generic information that the user can copy to clipboard
     * @constructor Create empty Copy to clipboard generic info builder
     */
    interface CopyToClipboardGenericInfoBuilder { fun buildGenericInfo() : String }

    // --------------------- Builder ---------------------

    class Builder internal constructor(
        internal var type: LogType = LogType.Debug,
        private var duration: Int = ShortToastDuration,
        private var message: String = "",
        private var extraInfo: String? = null) {

        fun setDuration(duration: Int) = apply { this@Builder.duration = duration }

        fun withMessage(message: String) = apply { this@Builder.message = message }

        fun withExtraInfo(extraInfo: String) = apply { this@Builder.extraInfo = extraInfo }

        @JvmOverloads
        fun show(activity: Activity? = null, genericInfoBuilder: CopyToClipboardGenericInfoBuilder? = null) {

            activity?.run {
                // Update activity reference
                instance.actReference = WeakReference(this)
            }

            genericInfoBuilder?.run {
                instance.copyGenericInfoBuilder = this
            }

            instance.add(LogDataHolder(message, duration.toLong(), type, extraInfo))
        }
    }

}
