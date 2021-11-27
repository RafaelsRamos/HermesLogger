package com.spartancookie.hermeslogger.core

import com.spartancookie.hermeslogger.data.InfoHolder
import com.spartancookie.hermeslogger.models.LogDataHolder

internal object HermesHandler {

    val infoHolder = InfoHolder()

    /**
     * Add Log information to the queue
     * @param dataHolder Log information
     */
    fun add(dataHolder: LogDataHolder) {
        // Add log to the list of logs
        infoHolder.addInfo(dataHolder)
    }
}