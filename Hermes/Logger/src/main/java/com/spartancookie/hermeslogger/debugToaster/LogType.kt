package com.spartancookie.hermeslogger.debugToaster

import androidx.annotation.DrawableRes
import com.spartancookie.hermeslogger.R

internal enum class LogType(@JvmField @DrawableRes val drawableResource: Int) {
    Info(R.drawable.ic_hermes_logger_info),
    Success(R.drawable.ic_hermes_logger_success),
    Debug(R.drawable.ic_hermes_logger_debug),
    Warning(R.drawable.ic_hermes_logger_warning),
    Error(R.drawable.ic_hermes_logger_error)
}