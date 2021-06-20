package com.example.myapplication

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.debugToaster.LongToastDuration
import com.example.myapplication.debugToaster.ShortToastDuration
import com.example.myapplication.debugToaster.Toaster
import com.example.myapplication.ui.components.OverviewLayout

private const val GENERATE_LOGS_TEXT = "Generate automatic logs"
private const val STOP_GENERATING_LOGS_TEXT = "Stop generating automatic logs"

private const val MAX_DURATION = 5000L
private const val FIRE_LOGS_DURATION = 7500L

class MainActivity : AppCompatActivity(), Toaster.CopyToClipboardGenericInfoBuilder, View.OnClickListener {

    private val passActivityCheckBox by lazy { findViewById<CheckBox>(R.id.pass_activity_cb) }
    private val radioGroup by lazy { findViewById<RadioGroup>(R.id.duration_radio_group) }
    private val durationEditText by lazy { findViewById<EditText>(R.id.durationEditText) }
    private val messageEditText by lazy { findViewById<EditText>(R.id.edit_text_message) }
    private val extraInfoEditText by lazy { findViewById<EditText>(R.id.edit_text_extra_message) }
    private val generateLogsButton by lazy { findViewById<Button>(R.id.generate_logs_button) }

    private var handler = Handler(Looper.getMainLooper())
    private var canGenerateRandomLogs = false

    private val durationText get() = durationEditText.text.toString().toIntOrNull() ?: LongToastDuration
    private val messageText get() = messageEditText.text.toString()
    private val extraMessageText get() = extraInfoEditText.text.toString()

    private val canPassActivity get() = passActivityCheckBox.isChecked

    private val duration get() = when (radioGroup.checkedRadioButtonId) {
        R.id.longRB -> LongToastDuration
        R.id.shortRB -> ShortToastDuration
        R.id.customRB -> durationText
        else -> LongToastDuration
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)

        findViewById<View>(R.id.button_success).setOnClickListener(this)
        findViewById<View>(R.id.button_info).setOnClickListener(this)
        findViewById<View>(R.id.button_debug).setOnClickListener(this)
        findViewById<View>(R.id.button_warning).setOnClickListener(this)
        findViewById<View>(R.id.button_error).setOnClickListener(this)
        generateLogsButton.setOnClickListener(this)

        radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.longRB -> durationEditText.visibility = View.GONE
                R.id.shortRB -> durationEditText.visibility = View.GONE
                R.id.customRB -> durationEditText.visibility = View.VISIBLE
            }
        }

        // Add OverviewLayout
        OverviewLayout.create(this)
    }

    override fun buildGenericInfo(): String {
        return buildString {
            append("System info: ")
            append("AppVersion: PlaceHolder-0.1.5 ")
            append("Device: ${Build.BRAND}-${Build.MODEL}")
            append("Device display metrics: ${Resources.getSystem().displayMetrics}")
        }
    }

    // -------------------------------------------------------------

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_success -> buildInfo(Toaster.success())
            R.id.button_info -> buildInfo(Toaster.info())
            R.id.button_debug -> buildInfo(Toaster.debug())
            R.id.button_warning -> buildInfo(Toaster.warning())
            R.id.button_error -> buildInfo(Toaster.error())
            R.id.generate_logs_button -> {
                canGenerateRandomLogs = !canGenerateRandomLogs

                if (canGenerateRandomLogs) {
                    generateLogsButton.text = STOP_GENERATING_LOGS_TEXT
                    scheduleAutomaticLogs()
                } else {
                    generateLogsButton.text = GENERATE_LOGS_TEXT
                    handler.removeCallbacksAndMessages(null)
                }
            }
        }
    }

    // -------------------------- Random samples --------------------------

    private val successLogSample by lazy {
        Runnable {
            Toaster.success().withMessage("Success sample message").withExtraInfo("Success extra info").show(this, this)
        }
    }

    private val errorLogSample by lazy {
        Runnable {
            Toaster.error().withMessage("Error sample message").withExtraInfo("Error extra info").show(this, this)
        }
    }

    private val warningLogSample by lazy {
        Runnable {
            Toaster.warning().withMessage("Warning sample message").withExtraInfo("Warning extra info").show(this, this)
        }
    }

    private val infoLogSample by lazy {
        Runnable {
            Toaster.info().withMessage("Info sample message").withExtraInfo("Info extra info").show(this, this)
        }
    }

    private val debugLogSample by lazy {
        Runnable {
            Toaster.debug().withMessage("Debug sample message").withExtraInfo("Debug extra info").show(this, this)
        }
    }

    private val scheduleFireLogs by lazy {
        Runnable {
            scheduleAutomaticLogs()
        }
    }


    // -------------------------- Helper methods --------------------------

    private fun scheduleAutomaticLogs() {
        Math.random()
        handler.postDelayed(successLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(errorLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(warningLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(infoLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(debugLogSample, (Math.random() * MAX_DURATION).toLong())

        // Reschedule firing logs
        handler.postDelayed(scheduleFireLogs, FIRE_LOGS_DURATION)
    }

    private fun buildInfo(toasterBuilder: Toaster.Builder) {
        toasterBuilder.apply {
            withMessage(messageText)
            setDuration(duration)

            if (extraMessageText.isNotEmpty()) {
                withExtraInfo(extraMessageText)
            }
        }.show(if (canPassActivity) this else null, this)

    }
}