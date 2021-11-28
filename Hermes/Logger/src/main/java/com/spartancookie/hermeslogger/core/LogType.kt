package com.spartancookie.hermeslogger.core

import androidx.annotation.DrawableRes
import com.spartancookie.hermeslogger.R

internal enum class LogType(@JvmField @DrawableRes val drawableResource: Int, val commentPrefix: String) {
    Success(R.drawable.ic_hermes_logger_success, "S-"),
    Info(R.drawable.ic_hermes_logger_info, "I-"),
    Debug(R.drawable.ic_hermes_logger_debug, "D-"),
    Warning(R.drawable.ic_hermes_logger_warning, "W-"),
    Error(R.drawable.ic_hermes_logger_error, "E-"),
}