package com.spartancookie.hermeslogger.core

import com.spartancookie.formatter.DataType

/**
 * ## Object to be used to register Hermes events.
 */
object Hermes {

    /**
     * ### Create an instance of [HermesBuilder] with a priority of [EventType.Success]
     */
    @JvmStatic
    fun success(): HermesBuilder = HermesBuilder(type = EventType.Success)

    /**
     * ### Submit a Hermes event of priority [EventType.Success], with:
     * - **Event short message - [message]**
     * - **Event short message - [message]**
     * - **Event description - [description]**
     * - **Event description's type of data - [dataType]**
     * - **List of tags associated with the event - [tags]**
     */
    @JvmStatic
    @JvmOverloads
    fun success(
        message: String,
        description: String? = null,
        dataType: DataType? = null,
        tags: List<String> = listOf()
    ) = success().submitData(message, description, dataType, tags)

    /**
     * ### Create an instance of [HermesBuilder] with a priority of [EventType.Verbose]
     */
    @JvmStatic
    fun v(): HermesBuilder = HermesBuilder(type = EventType.Verbose)

    /**
     * ### Submit a Hermes event of priority [EventType.Verbose], with:
     * - **Event short message - [message]**
     * - **Event description - [description]**
     * - **Event description's type of data - [dataType]**
     * - **List of tags associated with the event - [tags]**
     */
    @JvmStatic
    @JvmOverloads
    fun v(
        message: String,
        description: String? = null,
        dataType: DataType? = null,
        tags: List<String> = listOf()
    ) = v().submitData(message, description, dataType, tags)

    /**
     * ### Create an instance of [HermesBuilder] with a priority of [EventType.Debug]
     */
    @JvmStatic
    fun d(): HermesBuilder = HermesBuilder(type = EventType.Debug)

    /**
     * ### Submit a Hermes event of priority [EventType.Debug], with:
     * - **Event short message - [message]**
     * - **Event description - [description]**
     * - **Event description's type of data - [dataType]**
     * - **List of tags associated with the event - [tags]**
     */
    @JvmStatic
    @JvmOverloads
    fun d(
        message: String,
        description: String? = null,
        dataType: DataType? = null,
        tags: List<String> = listOf()
    ) = d().submitData(message, description, dataType, tags)

    /**
     * ### Create an instance of [HermesBuilder] with a priority of [EventType.Info]
     */
    @JvmStatic
    fun i(): HermesBuilder = HermesBuilder(type = EventType.Info)

    /**
     * ### Submit a Hermes event of priority [EventType.Info], with:
     * - **Event short message - [message]**
     * - **Event description - [description]**
     * - **Event description's type of data - [dataType]**
     * - **List of tags associated with the event - [tags]**
     */
    @JvmStatic
    @JvmOverloads
    fun i(
        message: String,
        description: String? = null,
        dataType: DataType? = null,
        tags: List<String> = listOf()
    ) = i().submitData(message, description, dataType, tags)

    /**
     * ### Create an instance of [HermesBuilder] with a priority of [EventType.Warning]
     */
    @JvmStatic
    fun w(): HermesBuilder = HermesBuilder(type = EventType.Warning)

    /**
     * ### Submit a Hermes event of priority [EventType.Warning], with:
     * - **Event short message - [message]**
     * - **Event description - [description]**
     * - **Event description's type of data - [dataType]**
     * - **List of tags associated with the event - [tags]**
     */
    @JvmStatic
    @JvmOverloads
    fun w(
        message: String,
        description: String? = null,
        dataType: DataType? = null,
        tags: List<String> = listOf()
    ) = w().submitData(message, description, dataType, tags)

    /**
     * ### Create an instance of [HermesBuilder] with a priority of [EventType.Error]
     */
    @JvmStatic
    fun e(): HermesBuilder = HermesBuilder(type = EventType.Error)

    /**
     * ### Submit a Hermes event of priority [EventType.Error], with:
     * - **Event short message - [message]**
     * - **Event description - [description]**
     * - **Event description's type of data - [dataType]**
     * - **List of tags associated with the event - [tags]**
     */
    @JvmStatic
    @JvmOverloads
    fun e(
        message: String,
        description: String? = null,
        dataType: DataType? = null,
        tags: List<String> = listOf()
    ) = e().submitData(message, description, dataType, tags)

    /**
     * ### Create an instance of [HermesBuilder] with a priority of [EventType.Wtf]
     */
    @JvmStatic
    fun wtf(): HermesBuilder = HermesBuilder(type = EventType.Wtf)

    /**
     * ### Submit a Hermes event of priority [EventType.Wtf], with:
     * - **Event short message - [message]**
     * - **Event description - [description]**
     * - **Event description's type of data - [dataType]**
     * - **List of tags associated with the event - [tags]**
     */
    @JvmStatic
    @JvmOverloads
    fun wtf(
        message: String,
        description: String? = null,
        dataType: DataType? = null,
        tags: List<String> = listOf()
    ) = wtf().submitData(message, description, dataType, tags)

    private fun HermesBuilder.submitData(msg: String, description: String?, dataType: DataType?, tags: List<String>) {
        this.tags(*tags.toTypedArray())
        this.message(msg)
        if (description != null) {
            this.description(description, dataType)
        }
        submit()
    }
}