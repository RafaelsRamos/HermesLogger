package com.spartancookie.hermeslogger.core

import com.spartancookie.formatter.DataType
import com.spartancookie.hermeslogger.models.LogDataHolder

class HermesBuilder internal constructor(
    internal var type: LogType = LogType.Debug,
    private var message: String = "",
    private var extraInfo: String? = null,
    private var dataType: DataType? = null
) {

    fun message(message: String) = apply { this@HermesBuilder.message = message }

    fun extraInfo(extraInfo: String) = apply { this@HermesBuilder.extraInfo = extraInfo }

    fun format(dataType: DataType) = apply { this@HermesBuilder.dataType = dataType }

    /**
     * Create a log with the parameters built and add it to the list of logs
     * can be seen through the [OverviewLayout]
     */
    fun addToList() {

        if (!HermesConfigurations.isEnabled) {
            return
        }

        val infoSnapshot = fetchSystemSnapshot()
        val log = LogDataHolder(message, type, extraInfo, genericInfo = infoSnapshot, dataType = dataType)
        HermesHandler.add(log)
    }
}