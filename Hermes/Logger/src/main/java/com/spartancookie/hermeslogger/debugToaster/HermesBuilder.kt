package com.spartancookie.hermeslogger.debugToaster

import android.app.Activity
import com.spartancookie.formatter.DataType
import com.spartancookie.hermeslogger.models.LogDataHolder
import java.lang.ref.WeakReference

class HermesBuilder internal constructor(
    internal var type: LogType = LogType.Debug,
    private var duration: Int = Hermes.SHORT_TOAST_DURATION,
    private var message: String = "",
    private var extraInfo: String? = null,
    private var dataType: DataType? = null) {

    fun duration(duration: Int) = apply { this@HermesBuilder.duration = duration }

    fun message(message: String) = apply { this@HermesBuilder.message = message }

    fun extraInfo(extraInfo: String) = apply { this@HermesBuilder.extraInfo = extraInfo }

    fun format(dataType: DataType) = apply { this@HermesBuilder.dataType = dataType }

    /**
     * Create a log with the parameters built and add it to the queue of Toasts being shown and to
     * the list of logs that can be seen through the [OverviewLayout]
     * @param activity Possible reference to an activity. If not null, update the activity's
     *                 weak reference instance.
     */
    @JvmOverloads
    fun addToQueue(activity: Activity? = null) {

        if (!HermesConfigurations.isEnabled) {
            return
        }

        activity?.run {
            // Update activity reference
            Hermes.actReference = WeakReference(this)
        }

        addLog(true)
    }

    /**
     * Create a log with the parameters built and add it to the list of logs, without showing a toast
     */
    fun addToList() {

        if (!HermesConfigurations.isEnabled) {
            return
        }

        addLog(false)
    }

    private fun addLog(showToast: Boolean) {
        val infoSnapshot = fetchSystemSnapshot()
        val log = LogDataHolder(message, duration.toLong(), type, extraInfo, genericInfo = infoSnapshot, dataType = dataType)
        HermesHandler.add(showToast, log)
    }
}