package com.rafaelsramos.hermes.data

import androidx.lifecycle.MutableLiveData
import com.rafaelsramos.hermes.debugToaster.LogType
import com.rafaelsramos.hermes.models.LogDataHolder
import com.rafaelsramos.hermes.utils.default

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
    val logList : MutableList<LogDataHolder> = mutableListOf()

    private var nrOfDebugs = 0
    private var nrOfSuccesses = 0
    private var nrOfWarnings = 0
    private var nrOfErrors = 0
    private var nrOfInfo = 0

    //------------------------ Controls ------------------------

    /**
     * Get the list of logs (List<[LogDataHolder]>) for a specific [LogType]
     * @param type  Log type
     */
    fun getLogListByType(type: LogType) = logList.filter { it.type == type }

    fun getNrOfLogsByType(type: LogType) = when(type) {
            LogType.Debug -> nrOfDebugs
            LogType.Success -> nrOfSuccesses
            LogType.Warning -> nrOfWarnings
            LogType.Error -> nrOfErrors
            LogType.Info -> nrOfInfo
        }

    /**
     * Add log into the list
     * @param log log
     */
    fun addInfo(log: LogDataHolder) {
        log.id = getValidID(log.type)
        logList.add(log)
        infoLiveData.postValue(logList)
    }

    //------------------------ Helper methods ------------------------

    /**
     * Get a valid ID and increase the counter of the number of logs of type [LogType]
     * @param type Log type
     * @return Valid ID
     */
    private fun getValidID(type: LogType) = when (type) {
        LogType.Debug -> "D-${++nrOfDebugs}"
        LogType.Success -> "S-${++nrOfSuccesses}"
        LogType.Warning -> "W-${++nrOfWarnings}"
        LogType.Error -> "E-${++nrOfErrors}"
        LogType.Info -> "I-${++nrOfInfo}"
    }

    private fun decreaseLogCountOfId(type: LogType) = when (type) {
        LogType.Debug -> --nrOfDebugs
        LogType.Success -> --nrOfSuccesses
        LogType.Warning -> --nrOfWarnings
        LogType.Error -> --nrOfErrors
        LogType.Info -> --nrOfInfo
    }

    private fun resetAllLogCounters() {
        nrOfDebugs = 0
        nrOfSuccesses = 0
        nrOfWarnings = 0
        nrOfErrors = 0
        nrOfInfo = 0
    }

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