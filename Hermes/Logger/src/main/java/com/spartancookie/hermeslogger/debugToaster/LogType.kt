package com.spartancookie.hermeslogger.debugToaster

import androidx.annotation.DrawableRes
import com.spartancookie.hermeslogger.R

internal enum class LogType(@JvmField @DrawableRes val drawableResource: Int, val commentPrefix: String) {
    Info(R.drawable.ic_hermes_logger_info, "I-"),
    Success(R.drawable.ic_hermes_logger_success, "S-"),
    Debug(R.drawable.ic_hermes_logger_debug, "D-"),
    Warning(R.drawable.ic_hermes_logger_warning, "W-"),
    Error(R.drawable.ic_hermes_logger_error, "E-")
}