package com.spartancookie.hermeslogger.data

import androidx.lifecycle.MutableLiveData
import com.spartancookie.hermeslogger.core.LogType
import com.spartancookie.hermeslogger.models.EventDataHolder
import com.spartancookie.hermeslogger.utils.default

/**
 * Class responsible for storing the logs adding during the session
 */
internal class InfoHolder {

    /**
     * LiveData instance that contains all the logs added during a session
     */
    val infoLiveData = MutableLiveData<MutableList<EventDataHolder>>().default(mutableListOf())

    /**
     * List of all logs
     */
    val eventList: MutableList<EventDataHolder> = mutableListOf()

    /**
     * HashMap responsible for holding information regarding the number
     * of logs for each type
     */
    private val logNumbers = LogsCounter()

    //------------------------ Controls ------------------------

    /**
     * Get the list of logs (List<[EventDataHolder]>) for a specific [LogType]
     * @param type  Log type
     */
    fun getLogListByType(type: LogType) = eventList.filter { it.type == type }


    fun getNumberOfLogsByType(type: LogType) = logNumbers[type]!!.get()

    /**
     * Add [event] into the list
     * @param event log that will be added to the list
     */
    fun addInfo(event: EventDataHolder) {
        event.id = getValidID(event.type)
        eventList.add(event)
        // Post value, to trigger update throughout the system
        infoLiveData.postValue(eventList)
    }

    //------------------------ Helper methods ------------------------

    /**
     * Get a valid ID and increase the counter of the number of logs of type [LogType]
     * @param type Log type that whose ID will be generated and whose count will be increased
     * @return Valid Log ID of the given [LogType]
     */
    private fun getValidID(type: LogType): String = "${type.commentPrefix}${logNumbers[type]!!.incrementAndGet()}"

    private fun decreaseLogCountOfType(type: LogType): Int = logNumbers[type]!!.decrementAndGet()

    /**
     * Remove log with the given [id] from the log list.
     * @param id ID of the log that will be removed.
     */
    fun removeLogById(id: String) {
        val indexOfLog = eventList.indexOfFirst { id == it.id }
        if (indexOfLog >= 0) {
            decreaseLogCountOfType(eventList[indexOfLog].type)
            eventList.removeAt(indexOfLog)
            infoLiveData.postValue(eventList)
        }
    }

    fun clearAllLogs() {
        eventList.clear()
        logNumbers.reset()
        infoLiveData.postValue(eventList)
    }

}