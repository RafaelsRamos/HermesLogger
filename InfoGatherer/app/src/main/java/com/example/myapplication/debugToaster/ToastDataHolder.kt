package com.example.myapplication.debugToaster

data class ToastDataHolder(val msg: String, val duration: Long, val type: ToastType, var extraInfo: String?)