package com.rafaelsramos.hermes

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.spartancookie.hermeslogger.debugToaster.LongToastDuration
import com.spartancookie.hermeslogger.debugToaster.ShortToastDuration
import com.spartancookie.hermeslogger.debugToaster.Toaster
import com.spartancookie.hermeslogger.ui.components.OverviewLayout

private const val BASE_DURATION_TEXT = "Duration between logs"

private const val GENERATE_LOGS_TEXT = "Generate automatic logs"
private const val STOP_GENERATING_LOGS_TEXT = "Stop generating automatic logs"

private const val MAX_DURATION = 5000L

class MainActivity : AppCompatActivity(), Toaster.SystemInfoBuildable, View.OnClickListener {

    private val durationMessage by lazy { findViewById<TextView>(R.id.duration_message) }
    private val durationSeekBar by lazy { findViewById<SeekBar>(R.id.simpleSeekBar) }

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

    private var fireLogDuration = 1000L

    private val duration get() = when (radioGroup.checkedRadioButtonId) {
        R.id.longRB -> LongToastDuration
        R.id.shortRB -> ShortToastDuration
        R.id.customRB -> durationText
        else -> LongToastDuration
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)

        setDurationSeekBar()

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

        // If we are in a debug environment, inform the toaster we are in one and initialize the OverviewLayout
        val isDebugEnvironment = true
        if (isDebugEnvironment) {
            Toaster.initialize(true)
            Toaster.updateSystemInfo(this)
            OverviewLayout.create(this)
        }

    }

    private fun setDurationSeekBar() {
        durationSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) { }
            override fun onStopTrackingTouch(seekBar: SeekBar) { }
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                fireLogDuration = progress.toLong()
                durationMessage.text = "$BASE_DURATION_TEXT $progress ms"
            }
        })
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

    private val successLogSample by lazy { Runnable { randomizeToaster(Toaster.success()) } }

    private val errorLogSample by lazy { Runnable { randomizeToaster(Toaster.error()) } }

    private val warningLogSample by lazy { Runnable { randomizeToaster(Toaster.warning()) } }

    private val infoLogSample by lazy { Runnable { randomizeToaster(Toaster.info()) } }

    private val debugLogSample by lazy { Runnable { randomizeToaster(Toaster.debug()) } }

    private val scheduleFireLogs by lazy { Runnable { scheduleAutomaticLogs() } }

    // -------------------------- Helper methods --------------------------

    private fun randomizeToaster(toastBuilder: Toaster.Builder) {
        toastBuilder.withMessage(RandomMessages.getSample).withExtraInfo(RandomExtraInfo.getSample)
        //.addToQueue(this)
            .addToList()
    }

    private fun scheduleAutomaticLogs() {
        Math.random()
        handler.postDelayed(successLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(errorLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(warningLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(infoLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(debugLogSample, (Math.random() * MAX_DURATION).toLong())

        // Reschedule firing logs
        handler.postDelayed(scheduleFireLogs, fireLogDuration)
    }

    private fun buildInfo(toasterBuilder: Toaster.Builder) {
        toasterBuilder.apply {
            withMessage(messageText)
            setDuration(duration)

            if (extraMessageText.isNotEmpty()) {
                withExtraInfo(extraMessageText)
            }
        }.addToQueue(if (canPassActivity) this else null)

    }
}