package com.spartancookie.hermeslogger.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.spartancookie.formatter.Formatter
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.models.LogDataHolder
import com.spartancookie.hermeslogger.ui.setCreationDate
import com.spartancookie.hermeslogger.ui.setLogIcon
import com.spartancookie.hermeslogger.utils.*
import com.spartancookie.hermeslogger.utils.animateCopyToClipboardColor
import com.spartancookie.hermeslogger.utils.copyToClipboard
import com.spartancookie.hermeslogger.utils.removeFromStack
import com.spartancookie.hermeslogger.utils.shareLog

internal class InfoDetailedViewFragment(private val item: LogDataHolder) : Fragment(R.layout.screen_detailed_view) {

    internal companion object {
        const val TAG =  "InfoDetailedViewFragment"
    }

    private lateinit var rootLayout: View
    private val backIV by lazy { rootLayout.findViewById<ImageView>(R.id.back) }
    private val copyToClipboardIV by lazy { rootLayout.findViewById<ImageView>(R.id.copy_to_clipboard) }
    private val shareLogIV by lazy { rootLayout.findViewById<ImageView>(R.id.share_icon) }
    private val typeIconIV by lazy { rootLayout.findViewById<ImageView>(R.id.type_icon) }
    private val typeNameTV by lazy { rootLayout.findViewById<TextView>(R.id.typeName) }
    private val dateTV by lazy { rootLayout.findViewById<TextView>(R.id.date) }
    private val messageTV by lazy { rootLayout.findViewById<TextView>(R.id.message) }
    private val extraInfoTV by lazy { rootLayout.findViewById<TextView>(R.id.extra_info) }
    private val genericInfoTV by lazy { rootLayout.findViewById<TextView>(R.id.generic_info) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        rootLayout = view

        // Set listeners
        backIV.setOnClickListener { removeFromStack(parentFragmentManager, TAG) }
        copyToClipboardIV.setOnClickListener { imageView ->
            animateCopyToClipboardColor(imageView)
            activity?.let { copyToClipboard(it, item) }
        }

        shareLogIV.run {
            if (hasWriteStoragePermission(context)) {
                setOnClickListener { context?.run { shareLog(this, item) } }
            } else {
                visibility = GONE
            }
        }

        // Fill views with log data
        typeIconIV.setLogIcon(item.type)
        dateTV.setCreationDate(item)
        typeNameTV.text = item.type.name
        messageTV.text = item.message
        genericInfoTV.text = item.genericInfo

        item.extraInfo?.let { extraInfo ->
            extraInfoTV.text = item.dataType?.let { Formatter.format(it, extraInfo) } ?: extraInfo
        }
    }
}