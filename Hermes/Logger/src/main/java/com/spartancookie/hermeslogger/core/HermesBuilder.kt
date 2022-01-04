package com.spartancookie.hermeslogger.core

import com.spartancookie.formatter.DataType
import com.spartancookie.hermeslogger.core.models.EventDataHolder

class HermesBuilder internal constructor(
    internal var type: EventType = EventType.Debug,
    private var message: String = "",
    private var description: String? = null,
    private var dataType: DataType? = null,
    private var throwable: Throwable? = null,
    private var tags: MutableList<String> = mutableListOf()
) {

    /**
     * Store the given [t] to be passed to [EventDataHolder] instance that will be created.
     *
     * Additionally, if no [message] was set thus far, the type of exception thrown will be
     * set as the [message].
     */
    fun throwable(t: Throwable) = apply {
        this@HermesBuilder.throwable = t

        val className = t.javaClass.canonicalName
        if (message.isEmpty() && className != null) {
            message = className
        }
    }

    /**
     * Store the given [message] and [dataType] to be passed to [EventDataHolder] instance that will be created.
     *
     * On the Overview layout, the [message] will be on display. It is advised to pass a very shot message
     * that allows the QA/Tester/Dev to identify where the log originated from and/or what is it about.
     * e.g.:
     * - NullReferenceException on MainActivity
     * - API call on DetailsFragment
     * - Load complete on SettingsFragment
     */
    fun message(message: String) = apply { this@HermesBuilder.message = message }

    /**
     * Store the given [description] to be passed to [EventDataHolder] instance that will be created.
     *
     * It is advised to pass in relevant information that can help either the QA/Tester/Dev identify
     * possible issues and/or confirm behaviour.
     *
     * e.g.:
     * - App start, general configuration:\n location - Portugal\n rank - Diamond\n has profile filled - true
     * - Loading the list of products. There are 7 products, 4 of them are coffee mugs;
     * - API call response received: {$response.data}
     *
     * [format] is used to format XML or Json data. Can be used to format API calls' responses, to make it
     * easier for QAs/Testers/Devs to read its content.
     *
     * To have a correctly formatted message, assure that the [description] only has the Json or XML content
     * and nothing else. Otherwise the formatter will fail to recognize the content as a valid Json or XML
     */
    @JvmOverloads
    fun description(description: String, format: DataType? = null) = apply {
        this@HermesBuilder.description = description
        this@HermesBuilder.dataType = format
    }

    /**
     * Try adding [tag] into the list of tags for this event.
     */
    fun tag(tag: String) = apply {
        if (!tags.contains(tag)) {
            tags.add(tag)
        }
    }

    /**
     * Try adding all the given [tags] into the list of tags for this event.
     */
    fun tags(vararg tags: String) = apply {
        tags.forEach {
            tag(it)
        }
    }

    /**
     * Create a log with the parameters built and add it to the list of logs
     * can be seen through the [com.spartancookie.hermeslogger.ui.components.OverviewLayout]
     */
    fun submit() {
        if (!HermesConfigurations.isEnabled) {
            return
        }

        HermesHandler.add(createLogDataHolder())
    }

    private fun createLogDataHolder() = EventDataHolder(
        type = type,
        message = message,
        description = description,
        genericInfo = tryTakingSystemSnapshot(),
        dataType = dataType,
        throwable = throwable,
        tags = tags
    )
}