package com.example.myapplication.data

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.debugToaster.LogType
import com.example.myapplication.models.InfoDataHolder
import com.example.myapplication.utils.default

/**
 * Class responsible for storing the logs adding during the session
 */
class InfoHolder {

    /**
     * LiveData instance that contains all the logs added during a session
     */
    val infoLiveData = MutableLiveData<MutableList<InfoDataHolder>>().default(mutableListOf())

    private val infoList : MutableList<InfoDataHolder> = mutableListOf()

    /**
     * Get the list of logs (List<[InfoDataHolder]>) for a specific [LogType]
     * @param type  Log type
     */
    fun getInfoByType(type: LogType?) = type?.let { t -> infoList.filter { t == type } } ?: infoList

    /**
     * Add log into the list
     * @param infoHolder log
     */
    fun addInfo(infoHolder: InfoDataHolder) {
        infoList.add(infoHolder)
        infoLiveData.postValue(infoList)
    }

}