package com.example.myapplication.data

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.debugToaster.ToastType
import com.example.myapplication.models.InfoDataHolder

class InfoHolder {

    val infoLiveData = MutableLiveData<MutableList<InfoDataHolder>>().default(mutableListOf())

    private val infoList : MutableList<InfoDataHolder> = mutableListOf()

    fun getInfoByType(type: ToastType) = infoList.filter { it.type == type }

    fun addInfo(infoHolder: InfoDataHolder) {
        infoList.add(infoHolder)
        infoLiveData.postValue(infoList)
    }

}

fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }