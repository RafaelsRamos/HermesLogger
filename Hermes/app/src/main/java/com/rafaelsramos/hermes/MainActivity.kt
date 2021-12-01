package com.rafaelsramos.hermes

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.res.Resources
import android.os.*
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.spartancookie.formatter.DataType
import com.spartancookie.hermeslogger.core.Hermes
import com.spartancookie.hermeslogger.core.HermesBuilder
import com.spartancookie.hermeslogger.core.SystemInfoBuildable
import com.spartancookie.hermeslogger.ui.components.OverviewLayout
import com.spartancookie.hermeslogger.utils.canShareLogDumps
import kotlinx.android.synthetic.main.test_activity.*

private const val BASE_DURATION_TEXT = "Duration between logs"

private const val GENERATE_LOGS_TEXT = "Generate automatic logs"
private const val STOP_GENERATING_LOGS_TEXT = "Stop generating automatic logs"

private const val MAX_DURATION = 5000L

class MainActivity : AppCompatActivity(), SystemInfoBuildable, View.OnClickListener {

    private var handler = Handler(Looper.getMainLooper())
    private var canGenerateRandomLogs = false

    private val messageText get() = edit_text_message.text.toString()
    private val extraMessageText get() = edit_text_extra_message.text.toString()

    private var fireLogDuration = 1000L

    private val randomExtraInfo get() = when (extra_info_type_radio_group.checkedRadioButtonId) {
        R.id.xml_rb -> RandomExtraInfo.getXMLSample
        R.id.json_rb -> RandomExtraInfo.getJSONSample
        else -> RandomExtraInfo.getSample
    }

    private val dataType get() = when (extra_info_type_radio_group.checkedRadioButtonId) {
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
        generate_logs_button.setOnClickListener(this)

        if (canShareLogDumps(applicationContext)) {
            initializeOverlay()
        } else {
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

    override fun buildSystemSnapshotInfo(): String {
        return buildString {
            append("System info: \n")
            append("AppVersion: PlaceHolder-1.1.5 \n")
            append("Device: ${Build.BRAND}-${Build.MODEL}\n")
            append("Device display metrics: ${Resources.getSystem().displayMetrics}\n")
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
                    generate_logs_button.text = STOP_GENERATING_LOGS_TEXT
                    scheduleAutomaticLogs()
                } else {
                    generate_logs_button.text = GENERATE_LOGS_TEXT
                    handler.removeCallbacksAndMessages(null)
                }
            }
        }
    }

    // -------------------------- Random samples --------------------------

    private val successLogSample by lazy { Runnable { randomizeInfo(Hermes.success()) } }

    private val errorLogSample by lazy { Runnable { randomizeInfo(Hermes.e()) } }

    private val warningLogSample by lazy { Runnable { randomizeInfo(Hermes.w()) } }

    private val infoLogSample by lazy { Runnable { randomizeInfo(Hermes.i()) } }

    private val debugLogSample by lazy { Runnable { randomizeInfo(Hermes.d()) } }

    private val scheduleFireLogs by lazy { Runnable { scheduleAutomaticLogs() } }

    // -------------------------- Helper methods --------------------------

    private fun initializeOverlay() {
        // If we are in a debug environment, inform the Hermes we are in one and initialize the OverviewLayout
        val isDebugEnvironment = true
        if (isDebugEnvironment) {
            Hermes.initialize(true)
            Hermes.updateSystemInfo(this)
            OverviewLayout.create(this)
        }

    }

    private fun randomizeInfo(builder: HermesBuilder) {
        builder
            .message(RandomMessages.getSample)
            .extraInfo(randomExtraInfo, dataType)
            .submit()
    }

    private fun scheduleAutomaticLogs() {
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

            if (extraMessageText.isNotEmpty()) {
                extraInfo(extraMessageText)
            }
        }.submit()

    }
}