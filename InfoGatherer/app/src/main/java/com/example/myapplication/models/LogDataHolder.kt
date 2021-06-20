package com.example.myapplication.models

import com.example.myapplication.debugToaster.LogType
import java.util.*

data class LogDataHolder(val message: String, val duration: Long, val type: LogType, var extraInfo: String? = null, var genericInfo: String? = null) {

    val creationDate: Date = Calendar.getInstance().time
    var id: String = ""

}