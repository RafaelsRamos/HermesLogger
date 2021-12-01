package com.spartancookie.hermeslogger.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.fragment.app.Fragment
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.models.LogDataHolder
import com.spartancookie.hermeslogger.share.ShareHelperCommon.shareLog
import com.spartancookie.hermeslogger.ui.setCreationDate
import com.spartancookie.hermeslogger.ui.setLogIcon
import com.spartancookie.hermeslogger.utils.*
import kotlinx.android.synthetic.main.screen_detailed_view.*

private const val ITEM_ARG = "selected_item"

internal class InfoDetailedViewFragment : Fragment(R.layout.screen_detailed_view) {

    private lateinit var item: LogDataHolder

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getParcelable<LogDataHolder>(ITEM_ARG)?.let {
            item = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        savedInstanceState?.run {
            removeFromStack(parentFragmentManager, TAG)
        }

        setOnClickListeners()

        fillViews()
    }

    private fun setOnClickListeners() {
        back.setOnClickListener {
            removeFromStack(parentFragmentManager, TAG)
        }

        copy_to_clipboard.setOnClickListener { imageView ->
            animateCopyToClipboardColor(imageView)
            activity?.let { copyToClipboard(it, item) }
        }

        share_icon.run {
            if (canShareLogDumps(context)) {
                setOnClickListener { context?.run { shareLog(this, item) } }
            } else {
                visibility = GONE
            }
        }
    }

    private fun fillViews() {
        // Set icon according to item priority
        type_icon.setLogIcon(item.type)
        typeName.text = item.type.name
        date.setCreationDate(item)

        setMessage()
        setExtraInfo()
        setThrowableInfo()
        setSystemInfo()
    }

    private fun setMessage() {
        if (item.message.isNotEmpty()) {
            message.text = item.message
            message.visibility = View.VISIBLE
        }
    }

    private fun setExtraInfo() {
        val extraInfo = item.extraInfo ?: return

        extra_info.text = item.dataType?.format(extraInfo) ?: extraInfo
        extra_info.visibility = View.VISIBLE
    }

    private fun setThrowableInfo() {
        val t = item.throwable ?: return

        stacktrace.text = t.stackTraceToString()
        stacktrace.visibility = View.VISIBLE
    }

    private fun setSystemInfo() {
        val systemInfo = item.genericInfo ?: return

        generic_info.text = systemInfo
        generic_info.visibility = View.VISIBLE
    }

    internal companion object {

        const val TAG = "InfoDetailedViewFragment"

        fun newInstance(item: LogDataHolder) = InfoDetailedViewFragment().apply {
            arguments = Bundle().apply { putParcelable(ITEM_ARG, item) }
        }

    }
}