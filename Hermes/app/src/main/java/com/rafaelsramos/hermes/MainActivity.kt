package com.rafaelsramos.hermes

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.spartancookie.formatter.DataType
import com.spartancookie.hermeslogger.debugToaster.*
import com.spartancookie.hermeslogger.ui.components.OverviewLayout
import com.spartancookie.hermeslogger.utils.hasWriteStoragePermission

private const val BASE_DURATION_TEXT = "Duration between logs"

private const val GENERATE_LOGS_TEXT = "Generate automatic logs"
private const val STOP_GENERATING_LOGS_TEXT = "Stop generating automatic logs"

private const val MAX_DURATION = 5000L

class MainActivity : AppCompatActivity(), SystemInfoBuildable, View.OnClickListener {

    private val durationMessage by lazy { findViewById<TextView>(R.id.duration_message) }
    private val durationSeekBar by lazy { findViewById<SeekBar>(R.id.simpleSeekBar) }

    private val durationRadioGroup by lazy { findViewById<RadioGroup>(R.id.duration_radio_group) }
    private val durationEditText by lazy { findViewById<EditText>(R.id.durationEditText) }

    private val dataTypeRadioGroup by lazy { findViewById<RadioGroup>(R.id.extra_info_type_radio_group) }

    private val messageEditText by lazy { findViewById<EditText>(R.id.edit_text_message) }
    private val extraInfoEditText by lazy { findViewById<EditText>(R.id.edit_text_extra_message) }
    private val generateLogsButton by lazy { findViewById<Button>(R.id.generate_logs_button) }

    private var handler = Handler(Looper.getMainLooper())
    private var canGenerateRandomLogs = false

    private val durationText get() = durationEditText.text.toString().toIntOrNull() ?: Hermes.LONG_TOAST_DURATION
    private val messageText get() = messageEditText.text.toString()
    private val extraMessageText get() = extraInfoEditText.text.toString()

    private var fireLogDuration = 1000L

    private val duration get() = when (durationRadioGroup.checkedRadioButtonId) {
        R.id.shortRB -> Hermes.SHORT_TOAST_DURATION
        R.id.customRB -> durationText
        else -> Hermes.LONG_TOAST_DURATION
    }

    private val randomExtraInfo get() = when (dataTypeRadioGroup.checkedRadioButtonId) {
        R.id.xml_rb -> RandomExtraInfo.getXMLSample
        R.id.json_rb -> RandomExtraInfo.getJSONSample
        else -> RandomExtraInfo.getSample
    }

    private val dataType get() = when (dataTypeRadioGroup.checkedRadioButtonId) {
        R.id.xml_rb -> DataType.XML
        R.id.json_rb -> DataType.JSON
        else -> null
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

        durationRadioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.longRB -> durationEditText.visibility = View.GONE
                R.id.shortRB -> durationEditText.visibility = View.GONE
                R.id.customRB -> durationEditText.visibility = View.VISIBLE
            }
        }

        if (hasWriteStoragePermission(applicationContext)) {
            initializeOverlay()
        }

        TedPermission.with(this)
            .setPermissionListener(object: PermissionListener {
                override fun onPermissionGranted() {
                    initializeOverlay()
                }
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    initializeOverlay()
                }
            })
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(WRITE_EXTERNAL_STORAGE)
            .check()
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

    override fun buildSystemSnapshotInfo(): String {
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
            R.id.button_success -> buildInfo(Hermes.success())
            R.id.button_info -> buildInfo(Hermes.i())
            R.id.button_debug -> buildInfo(Hermes.d())
            R.id.button_warning -> buildInfo(Hermes.w())
            R.id.button_error -> buildInfo(Hermes.e())
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

    private val successLogSample by lazy { Runnable { randomizeToaster(Hermes.success()) } }

    private val errorLogSample by lazy { Runnable { randomizeToaster(Hermes.e()) } }

    private val warningLogSample by lazy { Runnable { randomizeToaster(Hermes.w()) } }

    private val infoLogSample by lazy { Runnable { randomizeToaster(Hermes.i()) } }

    private val debugLogSample by lazy { Runnable { randomizeToaster(Hermes.d()) } }

    private val scheduleFireLogs by lazy { Runnable { scheduleAutomaticLogs() } }

    // -------------------------- Helper methods --------------------------

    private fun initializeOverlay() {
        // If we are in a debug environment, inform the toaster we are in one and initialize the OverviewLayout
        val isDebugEnvironment = true
        if (isDebugEnvironment) {
            Hermes.initialize(true, this)
            Hermes.updateSystemInfo(this)
            OverviewLayout.create(this)
        }

    }

    private fun randomizeToaster(toastBuilder: HermesBuilder) {
        toastBuilder
            .message(RandomMessages.getSample)
            .extraInfo(randomExtraInfo)
            .apply {
                dataType?.run {
                    format(this)
                }
            }
        .addToQueue()
        //    .addToList()
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

    private fun buildInfo(hermesBuilder: HermesBuilder) {
        hermesBuilder.apply {
            message(messageText)
            duration(duration)

            if (extraMessageText.isNotEmpty()) {
                extraInfo(extraMessageText)
            }
        }.addToQueue(this)

    }
}