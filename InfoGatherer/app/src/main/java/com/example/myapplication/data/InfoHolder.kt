package com.example.myapplication.data

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.debugToaster.LogType
import com.example.myapplication.models.LogDataHolder
import com.example.myapplication.utils.default

/**
 * Class responsible for storing the logs adding during the session
 */
class InfoHolder {

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
    private var nrOfOthers = 0

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
            LogType.Info -> nrOfOthers
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
        LogType.Info -> "I-${++nrOfOthers}"
    }

}