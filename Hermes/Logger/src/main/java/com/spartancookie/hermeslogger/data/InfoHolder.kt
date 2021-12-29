package com.spartancookie.hermeslogger.data

import androidx.lifecycle.MutableLiveData
import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.filters.applyFilters
import com.spartancookie.hermeslogger.models.EventDataHolder
import com.spartancookie.hermeslogger.utils.default

/**
 * Class responsible for storing the logs adding during the session
 */
internal class InfoHolder {

    /**
     * LiveData instance that contains all the logs added during a session
     */
    val eventsReplacedLiveData = MutableLiveData<List<EventDataHolder>>().default(listOf())

    /**
     * LiveData instance that contains all the logs added during a session
     */
    val eventAddedLiveData = MutableLiveData<EventDataHolder>()

    private val _eventList: MutableList<EventDataHolder> = mutableListOf()

    /**
     * List of all logs, filtered
     */
    val eventList: List<EventDataHolder> get() = _eventList.applyFilters()

    val totalEventsCount get() = _eventList.count()

    val totalEventsShownCount get() = eventList.count()

    /**
     * HashMap responsible for holding information regarding the number
     * of logs for each type
     */
    private val logNumbers = LogsCounter()

    //------------------------ Controls ------------------------

    /**
     * Get List of [String] corresponding to all for all the items.
     */
    fun getTagsList(): List<String> {
        return mutableListOf<String>().apply {
            _eventList.forEach { event ->
                addAll(event.tags)
            }
        }.distinct()
    }

    fun getNumberOfLogsByType(type: EventType) = logNumbers[type]!!.get()

    /**
     * Add [event] into the list
     * @param event log that will be added to the list
     */
    fun addInfo(event: EventDataHolder) {
        event.id = getValidID(event.type)
        _eventList.add(event)
        // Post value, to trigger update throughout the system
        //infoLiveData.postValue(eventList)
        eventAddedLiveData.postValue(event)
    }

    //------------------------ Helper methods ------------------------

    /**
     * Get a valid ID and increase the counter of the number of logs of type [EventType]
     * @param type Log type that whose ID will be generated and whose count will be increased
     * @return Valid Log ID of the given [EventType]
     */
    private fun getValidID(type: EventType): String = "${type.commentPrefix}${logNumbers[type]!!.incrementAndGet()}"

    private fun decreaseLogCountOfType(type: EventType): Int = logNumbers[type]!!.decrementAndGet()

    /**
     * Remove log with the given [id] from the log list.
     * @param id ID of the log that will be removed.
     */
    fun removeLogById(id: String) {
        val indexOfLog = eventList.indexOfFirst { id == it.id }
        if (indexOfLog >= 0) {
            decreaseLogCountOfType(eventList[indexOfLog].type)
            _eventList.removeAt(indexOfLog)
        }
    }

    fun clearAllLogs() {
        _eventList.clear()
        logNumbers.reset()
        eventsReplacedLiveData.postValue(eventList)
    }

}