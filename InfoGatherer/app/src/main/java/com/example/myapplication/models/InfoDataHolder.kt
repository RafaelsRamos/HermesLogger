package com.example.myapplication.models

import com.example.myapplication.debugToaster.ToastType
import java.util.*

data class InfoDataHolder(val msg: String, val duration: Long, val type: ToastType, var extraInfo: String?) {

    val creationDate: Date = Calendar.getInstance().time

}