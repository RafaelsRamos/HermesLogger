package com.spartancookie.hermeslogger.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.fragment.app.Fragment
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.models.LogDataHolder
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

        back.setOnClickListener {
            removeFromStack(parentFragmentManager, TAG)
        }

        copy_to_clipboard.setOnClickListener { imageView ->
            animateCopyToClipboardColor(imageView)
            activity?.let { copyToClipboard(it, item) }
        }

        share_icon.run {
            if (hasWriteStoragePermission(context)) {
                setOnClickListener { context?.run { shareLog(this, item) } }
            } else {
                visibility = GONE
            }
        }

        type_icon.setLogIcon(item.type)

        typeName.text = item.type.name

        date.setCreationDate(item)

        message.text = item.message
        generic_info.text = item.genericInfo

        item.extraInfo?.let { extraInfo ->
            extra_info.text = item.dataType?.format(extraInfo) ?: extraInfo
        }
    }

    internal companion object {
        const val TAG = "InfoDetailedViewFragment"

        fun newInstance(item: LogDataHolder) = InfoDetailedViewFragment().apply {
            arguments = Bundle().apply { putParcelable(ITEM_ARG, item) }
        }
    }
}