package com.spartancookie.hermeslogger.debugToaster

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.spartancookie.hermeslogger.data.InfoHolder
import com.spartancookie.hermeslogger.models.LogDataHolder
import com.spartancookie.hermeslogger.utils.default
import java.util.*

internal object HermesHandler {

    private const val TAG = "HermesHandler"

    private val activity get() = Hermes.actReference?.get()
    private val hasItemsInQueue get() = toastQueue.size > 0

    val infoHolder = InfoHolder()

    // List of logs that will be displayed as toasts
    var toastQueue : LinkedList<LogDataHolder> = LinkedList()

    private var isShowing = false

    val hasToastsInQueue = MutableLiveData<Boolean>().default(false)

    fun refreshHasToastsLiveData() {
        hasToastsInQueue.postValue(hasItemsInQueue)
    }

    /**
     * Add Toast information to the queue
     * @param dataHolder Toast information
     * @param showToast  True to show bottom toast, False to hide it (Shows the toast by default)
     */
    fun add(showToast: Boolean, dataHolder: LogDataHolder) {
        // Add log to the list of logs
        infoHolder.addInfo(dataHolder)

        if (!showToast || !HermesConfigurations.areToastsEnabled) {
            return
        }

        // If we have a valid instance of the activity, show the toast
        activity?.run {
            toastQueue.add(buildGenericInfo(dataHolder))
            if (!isShowing) {
                hasToastsInQueue.postValue(hasItemsInQueue)
                showFirst()
            }
        } ?: onNoActivityReference()
    }

    // Jumpstarts the toasts display queue
    private fun showFirst() {
        if (!toastQueue.isEmpty()) {
            isShowing = true
            val dataHolder = toastQueue.first
            toastQueue.removeFirst()

            tryShowingToast(dataHolder)

            // Queue next toast
            Handler(Looper.getMainLooper())
                .postDelayed({ showFirst() }, dataHolder.duration)
        } else {
            isShowing = false
        }
    }

    private fun tryShowingToast(dataHolder: LogDataHolder) {
        activity?.run {
            DebugToast.show(this, dataHolder)
        } ?: onNoActivityReference()
    }

    private fun onNoActivityReference() {
        clearToastQueue()
        Log.w(TAG, "There is no valid instance of an activity. No toast will be shown.")
    }

    fun clearToastQueue() {
        toastQueue.clear()
        refreshHasToastsLiveData()
    }
}