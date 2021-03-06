package com.spartancookie.hermeslogger.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.core.models.EventDataHolder
import com.spartancookie.hermeslogger.utils.DateFormat
import com.spartancookie.hermeslogger.utils.NO_RES
import java.text.SimpleDateFormat

internal fun ImageView.setLogIcon(eventType: EventType, tintColorRes: Int = NO_RES) {
    setImageDrawable(ContextCompat.getDrawable(context, eventType.drawableResource))

    if (tintColorRes == NO_RES) return

    setColorFilter(ContextCompat.getColor(context, tintColorRes))
}

internal fun TextView.setCreationDate(dataHolder: EventDataHolder) {
    val dateFormat = SimpleDateFormat(DateFormat)
    text = dateFormat.format(dataHolder.creationDate.time)
}