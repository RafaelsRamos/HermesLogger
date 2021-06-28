package com.spartancookie.hermeslogger.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.spartancookie.hermeslogger.debugToaster.LogType
import com.spartancookie.hermeslogger.models.LogDataHolder
import com.spartancookie.hermeslogger.utils.DateFormat
import com.spartancookie.hermeslogger.utils.NO_RES
import java.text.SimpleDateFormat

internal fun ImageView.setLogIcon(logType: LogType, tintColorRes: Int = NO_RES) {
    setImageDrawable(ContextCompat.getDrawable(context, logType.drawableResource))

    if (tintColorRes == NO_RES)
        return

    setColorFilter(ContextCompat.getColor(context, tintColorRes))
}

internal fun TextView.setCreationDate(dataHolder: LogDataHolder) {
    val dateFormat = SimpleDateFormat(DateFormat)
    text = dateFormat.format(dataHolder.creationDate.time)
}