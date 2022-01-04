package com.spartancookie.hermeslogger.commands.ui.viewholders

import android.view.View
import android.widget.TextView
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.commands.models.HermesCommand
import com.spartancookie.hermeslogger.filters.ui.viewholders.BaseViewHolder

internal class CommandViewHolder(private val root: View): BaseViewHolder<HermesCommand>(root) {

    private val titleTV = root.findViewById<TextView>(R.id.title_tv)
    private val descriptionTV = root.findViewById<TextView>(R.id.description_tv)

    override fun bind(hermesCommand: HermesCommand) {

        titleTV.text = hermesCommand.name
        descriptionTV.text = hermesCommand.description
        root.setOnClickListener {
            hermesCommand.command.execute()
        }

    }

}