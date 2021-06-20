package com.example.myapplication.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.myapplication.debugToaster.LogType
import com.example.myapplication.utils.DateFormat
import com.example.myapplication.utils.NO_RES
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter(value = ["logIcon", "applyTint"], requireAll = false)
fun setLogIcon(imageView: ImageView, logType: LogType, tintColorRes: Int = NO_RES) {
    imageView.run {
        setImageDrawable(ContextCompat.getDrawable(context, logType.drawableResource))

        if (tintColorRes == NO_RES)
            return@run

        setColorFilter(ContextCompat.getColor(context, tintColorRes))
    }
}

@BindingAdapter(value = ["textCreationDate"], requireAll = false)
fun setCreationDate(textView: TextView, date: Date) {
    val dateFormat = SimpleDateFormat(DateFormat)
    textView.text = dateFormat.format(date.time)
}