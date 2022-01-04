package com.rafaelsramos.hermes.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.rafaelsramos.hermes.R
import com.spartancookie.hermeslogger.commands.models.HermesCommand
import com.spartancookie.hermeslogger.core.Hermes
import com.spartancookie.hermeslogger.core.HermesBuilder
import com.spartancookie.hermeslogger.core.HermesCentral
import kotlinx.android.synthetic.main.screen_single_trigger_fragment.*

class SingleTriggerFragment: BaseFragment(R.layout.screen_single_trigger_fragment) {

    private val messageText get() = edit_text_message.text.toString()
    private val extraMessageText get() = edit_text_extra_message.text.toString()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEventTriggers()

        back_button.setOnClickListener {
            activity?.onBackPressed()
        }

        val hermesCommands = mutableListOf(
            HermesCommand(name = "SingleTriggerFragment cmd 1", description = "Fragment only stuff.", path = "", command = { toast("Command 1") }),
            HermesCommand(name = "SingleTriggerFragment cmd 2", description = "Fragment only stuff.", path = "", command = { toast("Command 2") }),
            HermesCommand(name = "SingleTriggerFragment cmd 3", description = "Fragment only stuff.", path = "", command = { toast("Command 3") }),
            HermesCommand(name = "SingleTriggerFragment cmd 4", description = "Fragment only stuff.", path = "", command = { toast("Command 4") }),
            HermesCommand(name = "SingleTriggerFragment cmd 4", description = "Fragment only stuff.", path = "", command = { toast("Command 4") })
        )

        HermesCentral.addCommands(this, hermesCommands)
    }

    private fun toast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    private fun setEventTriggers() {
        button_success.setOnClickListener { buildInfo(Hermes.success()) }
        button_verbose.setOnClickListener { buildInfo(Hermes.v()) }
        button_info.setOnClickListener { buildInfo(Hermes.i()) }
        button_debug.setOnClickListener { buildInfo(Hermes.d()) }
        button_warning.setOnClickListener { buildInfo(Hermes.w()) }
        button_error.setOnClickListener { buildInfo(Hermes.e()) }
        button_wtf.setOnClickListener { buildInfo(Hermes.wtf()) }
    }

    private fun buildInfo(hermesBuilder: HermesBuilder) {
        hermesBuilder.apply {
            message(messageText)

            if (extraMessageText.isNotEmpty()) {
                description(extraMessageText)
            }
        }.submit()
    }
}