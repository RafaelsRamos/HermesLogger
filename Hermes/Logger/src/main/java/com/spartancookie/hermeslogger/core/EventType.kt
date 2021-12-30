package com.spartancookie.hermeslogger.core

import androidx.annotation.DrawableRes
import com.spartancookie.hermeslogger.R

/**
 * Possible types of events.
 */
internal enum class EventType(
    /**
     * Drawable resource, that will be used as the Icon of the Event type
     */
    @DrawableRes val drawableResource: Int,
    /**
     * Prefix that will be added to the ID of the log.
     */
    val commentPrefix: String
) {

    Success(R.drawable.ic_hermes_logger_success, "S-"),
    Verbose(R.drawable.ic_hermes_logger_verbose, "V-"),
    Info(R.drawable.ic_hermes_logger_info, "I-"),
    Debug(R.drawable.ic_hermes_logger_debug, "D-"),
    Warning(R.drawable.ic_hermes_logger_warning, "W-"),
    Error(R.drawable.ic_hermes_logger_error, "E-"),
    Wtf(R.drawable.ic_hermes_logger_wtf, "Wtf-"),

}