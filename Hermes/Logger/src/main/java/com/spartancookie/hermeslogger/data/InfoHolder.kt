package com.spartancookie.hermeslogger.data

import androidx.lifecycle.MutableLiveData
import com.spartancookie.hermeslogger.debugToaster.LogType
import com.spartancookie.hermeslogger.models.LogDataHolder
import com.spartancookie.hermeslogger.utils.default
import java.util.concurrent.atomic.AtomicInteger

/**
 * Class responsible for storing the logs adding during the session
 */
internal class InfoHolder {

    /**
     * LiveData instance that contains all the logs added during a session
     */
    val infoLiveData = MutableLiveData<MutableList<LogDataHolder>>().default(mutableListOf())

    /**
     * List of all logs
     */
    val logList: MutableList<LogDataHolder> = mutableListOf()

    private val logNumbers: HashMap<LogType, AtomicInteger> = hashMapOf(
        LogType.Debug to AtomicInteger(0),
        LogType.Success to AtomicInteger(0),
        LogType.Warning to AtomicInteger(0),
        LogType.Error to AtomicInteger(0),
        LogType.Info to AtomicInteger(0)
    )

    private val logsN = LogsCounter()

    //------------------------ Controls ------------------------

    /**
     * Get the list of logs (List<[LogDataHolder]>) for a specific [LogType]
     * @param type  Log type
     */
    fun getLogListByType(type: LogType) = logList.filter { it.type == type }


    fun getNrOfLogsByType(type: LogType) = logNumbers[type]!!.get()

    /**
     * Add [log] into the list
     * @param log log that will be added to the list
     */
    fun addInfo(log: LogDataHolder) {
        log.id = getValidID(log.type)
        logList.add(log)
        infoLiveData.postValue(logList)
    }

    //------------------------ Helper methods ------------------------

    /**
     * Get a valid ID and increase the counter of the number of logs of type [LogType]
     * @param type Log type that whose ID will be generated and whose count will be increased
     * @return Valid Log ID of the given [LogType]
     */
    private fun getValidID(type: LogType) = "${type.commentPrefix}${logNumbers[type]!!.incrementAndGet()}"

    private fun decreaseLogCountOfId(type: LogType): Int {
        return logNumbers[type]!!.decrementAndGet()
    }

    private fun resetAllLogCounters() {
        logNumbers.keys.forEach { key -> logNumbers[key]!!.set(0) }
    }

    /**
     * Remove log with the given [id] from the log list.
     * @param id ID of the log that will be removed.
     */
    fun removeLogById(id: String) {
        val indexOfLog = logList.indexOfFirst { id == it.id }
        if (indexOfLog >= 0) {
            decreaseLogCountOfId(logList[indexOfLog].type)
            logList.removeAt(indexOfLog)
            infoLiveData.postValue(logList)
        }
    }

    fun clearAllLogs() {
        logList.clear()
        resetAllLogCounters()
        infoLiveData.postValue(logList)
    }

}