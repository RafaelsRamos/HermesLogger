package com.example.myapplication.models

import com.example.myapplication.debugToaster.LogType
import java.util.*

data class InfoDataHolder(val msg: String, val duration: Long, val type: LogType, var extraInfo: String?) {

    val creationDate: Date = Calendar.getInstance().time

}