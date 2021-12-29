package com.rafaelsramos.hermes.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import com.rafaelsramos.hermes.R
import com.rafaelsramos.hermes.samples.RandomExtraInfo
import com.rafaelsramos.hermes.samples.RandomMessages
import com.rafaelsramos.hermes.samples.RandomTags
import com.spartancookie.formatter.DataType
import com.spartancookie.hermeslogger.core.Hermes
import com.spartancookie.hermeslogger.core.HermesBuilder
import kotlinx.android.synthetic.main.screen_automatic_trigger_fragment.*

private const val BASE_DURATION_TEXT = "Duration between logs"
private const val GENERATE_LOGS_TEXT = "Generate automatic logs"
private const val STOP_GENERATING_LOGS_TEXT = "Stop generating automatic logs"
private const val MAX_DURATION = 5000L

class AutomaticTriggerFragment: BaseFragment(R.layout.screen_automatic_trigger_fragment) {

    private var handler = Handler(Looper.getMainLooper())
    private var canGenerateRandomLogs = false

    private var fireLogDuration = 1000L

    private val randomExtraInfo get() = when (extra_info_type_radio_group.checkedRadioButtonId) {
        R.id.xml_rb  -> RandomExtraInfo.getXMLSample
        R.id.json_rb -> RandomExtraInfo.getJSONSample
        else -> RandomExtraInfo.getSample
    }

    private val dataType get() = when (extra_info_type_radio_group.checkedRadioButtonId) {
        R.id.xml_rb -> DataType.XML
        R.id.json_rb -> DataType.JSON
        else -> null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDurationSeekBar()

        generate_logs_button.setOnClickListener {
            canGenerateRandomLogs = !canGenerateRandomLogs
            if (canGenerateRandomLogs) {
                generate_logs_button.text = STOP_GENERATING_LOGS_TEXT
                scheduleAutomaticLogs()
            } else {
                generate_logs_button.text = GENERATE_LOGS_TEXT
                handler.removeCallbacksAndMessages(null)
            }
        }

        back_button.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setDurationSeekBar() {
        simpleSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) { }
            override fun onStopTrackingTouch(seekBar: SeekBar) { }
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                fireLogDuration = progress.toLong()
                duration_message.text = "$BASE_DURATION_TEXT $progress ms"
            }
        })
    }

    // -------------------------- Random samples --------------------------

    private val successLogSample by lazy { Runnable { randomizeInfo(Hermes.success()) } }

    private val verboseLogSample by lazy { Runnable { randomizeInfo(Hermes.v()) } }

    private val infoLogSample by lazy { Runnable { randomizeInfo(Hermes.i()) } }

    private val debugLogSample by lazy { Runnable { randomizeInfo(Hermes.d()) } }

    private val warningLogSample by lazy { Runnable { randomizeInfo(Hermes.w()) } }

    private val errorLogSample by lazy { Runnable { randomizeInfo(Hermes.e()) } }

    private val wtfLogSample by lazy { Runnable { randomizeInfo(Hermes.wtf()) } }

    private val scheduleFireLogs by lazy { Runnable { scheduleAutomaticLogs() } }

    private fun randomizeInfo(builder: HermesBuilder) {
        builder
            .tag("Random")
            .tags(*RandomTags.getRandomTags.toTypedArray())
            .message(RandomMessages.getSample)
            .extraInfo(randomExtraInfo, dataType)
            .submit()
    }

    private fun scheduleAutomaticLogs() {
        handler.postDelayed(successLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(verboseLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(infoLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(debugLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(warningLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(errorLogSample, (Math.random() * MAX_DURATION).toLong())
        handler.postDelayed(wtfLogSample, (Math.random() * MAX_DURATION).toLong())

        // Reschedule firing logs
        handler.postDelayed(scheduleFireLogs, fireLogDuration)
    }
}