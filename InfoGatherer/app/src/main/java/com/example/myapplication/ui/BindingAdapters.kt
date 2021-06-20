package com.example.myapplication.ui

import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import com.example.myapplication.R
import com.example.myapplication.debugToaster.LogType
import com.example.myapplication.utils.NO_RES

@BindingAdapter(value = ["logIcon", "applyTint"], requireAll = false)
fun setLogIcon(imageView: ImageView, logType: LogType, tintColorRes: Int = NO_RES) {
    imageView.run {
        setImageDrawable(ContextCompat.getDrawable(context, logType.drawableResource))

        if (tintColorRes == NO_RES)
            return@run

        setColorFilter(ContextCompat.getColor(context, tintColorRes))
    }
}