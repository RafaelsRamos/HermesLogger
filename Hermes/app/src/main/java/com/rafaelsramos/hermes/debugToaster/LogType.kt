package com.rafaelsramos.hermes.debugToaster

import androidx.annotation.DrawableRes
import com.rafaelsramos.hermes.R

internal enum class LogType(@JvmField @DrawableRes val drawableResource: Int) {
    Info(R.drawable.ic_info),
    Success(R.drawable.ic_success),
    Debug(R.drawable.ic_debug),
    Warning(R.drawable.ic_warning),
    Error(R.drawable.ic_error)
}