package com.spartancookie.hermeslogger.core

import com.spartancookie.formatter.DataType
import com.spartancookie.hermeslogger.models.LogDataHolder

class HermesBuilder internal constructor(
    internal var type: LogType = LogType.Debug,
    private var message: String = "",
    private var extraInfo: String? = null,
    private var dataType: DataType? = null,
    private var throwable: Throwable? = null
) {

    fun throwable(t: Throwable) = apply {
        this@HermesBuilder.throwable = t

        val className = t.javaClass.canonicalName
        if (message.isEmpty() && className != null) {
            message = className
        }
    }

    fun message(message: String) = apply { this@HermesBuilder.message = message }

    fun extraInfo(extraInfo: String) = apply { this@HermesBuilder.extraInfo = extraInfo }

    fun format(dataType: DataType) = apply { this@HermesBuilder.dataType = dataType }

    /**
     * Create a log with the parameters built and add it to the list of logs
     * can be seen through the [OverviewLayout]
     */
    fun submit() {

        if (!HermesConfigurations.isEnabled) {
            return
        }

        HermesHandler.add(createLogDataHolder())
    }

    private fun createLogDataHolder() = LogDataHolder(
        type = type,
        message = message,
        extraInfo = extraInfo,
        genericInfo = fetchSystemSnapshot(),
        dataType = dataType,
        throwable = throwable
    )
}