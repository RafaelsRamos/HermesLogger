package com.rafaelsramos.hermes.ui.fragments

import android.os.Bundle
import android.view.View
import com.rafaelsramos.hermes.R
import com.spartancookie.hermeslogger.core.Hermes
import com.spartancookie.hermeslogger.core.HermesBuilder
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
                extraInfo(extraMessageText)
            }
        }.submit()
    }
}