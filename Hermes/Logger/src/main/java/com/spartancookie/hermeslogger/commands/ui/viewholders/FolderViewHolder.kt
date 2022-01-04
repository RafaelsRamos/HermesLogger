package com.spartancookie.hermeslogger.commands.ui.viewholders

import android.view.View
import android.widget.TextView
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.commands.models.HermesFolder
import com.spartancookie.hermeslogger.filters.ui.viewholders.BaseViewHolder

internal class FolderViewHolder(val root: View): BaseViewHolder<HermesFolder>(root) {

    private val titleTV = root.findViewById<TextView>(R.id.title_tv)

    override fun bind(folder: HermesFolder) {
        titleTV.text = folder.name
    }

}