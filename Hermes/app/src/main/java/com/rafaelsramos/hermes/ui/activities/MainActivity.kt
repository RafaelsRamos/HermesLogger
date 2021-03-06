package com.rafaelsramos.hermes.ui.activities

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.res.Resources
import android.os.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.rafaelsramos.hermes.ICommunicationChannel
import com.rafaelsramos.hermes.R
import com.rafaelsramos.hermes.samples.CommandsSamples
import com.rafaelsramos.hermes.ui.fragments.BaseFragment
import com.rafaelsramos.hermes.ui.fragments.StartFragment
import com.rafaelsramos.hermes.utils.loadHermesPreference
import com.spartancookie.hermeslogger.core.Hermes
import com.spartancookie.hermeslogger.core.HermesCentral
import com.spartancookie.hermeslogger.core.HermesConfigurations
import com.spartancookie.hermeslogger.core.SystemInfoBuildable
import com.spartancookie.hermeslogger.ui.components.OverviewLayout
import com.spartancookie.hermeslogger.utils.canShareHermesLogDumps
import timber.log.Timber


class MainActivity : AppCompatActivity(), SystemInfoBuildable, ICommunicationChannel {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        HermesConfigurations.initialize(loadHermesPreference(baseContext))
        if (HermesConfigurations.isEnabled) {
            initializeHermes()
        }
        Timber.plant(Timber.DebugTree())

        supportFragmentManager.addOnBackStackChangedListener {
            val foregroundFragment = supportFragmentManager.fragments.last()
            val fragmentsCount = supportFragmentManager.backStackEntryCount + 1
            Hermes.i(
                message = "Fragment Backstack changed",
                description = "Fragments in stack: $fragmentsCount\nForeground fragment:$foregroundFragment",
                tags = listOf("Fragment-Backstack")
            )
        }

        setInitialFragment()

        HermesCentral.setCommands(CommandsSamples.getCommands(applicationContext))
    }

    private fun setInitialFragment() {
        val initialFragment = StartFragment().apply {
            communicationChannel = this@MainActivity
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.base_frame_layout, initialFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    // -------------------------- Helper methods --------------------------

    private fun initializeOverlay() {
        // If we are in a debug environment, inform the Hermes we are in one and initialize the OverviewLayout
        HermesConfigurations.initialize(true)
        HermesConfigurations.updateSystemInfo(this)
        OverviewLayout.create(this)
    }

    private fun initializeHermes() {
        if (canShareHermesLogDumps(this)) {
            initializeOverlay()
            Toast.makeText(baseContext, "Hermes initialized", Toast.LENGTH_SHORT).show()
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

    // -------------- SystemInfoBuildable implementation --------------

    override fun buildSystemSnapshotInfo(): String {
        return buildString {
            append("System info: \n")
            append("AppVersion: PlaceHolder-1.1.5 \n")
            append("Device: ${Build.BRAND}-${Build.MODEL}\n")
            append("Device display metrics: ${Resources.getSystem().displayMetrics}\n")
        }
    }

    // ----------- ICommunicationChannel implementation -----------

    override fun loadFragment(fragment: BaseFragment) {
        fragment.communicationChannel = this
        supportFragmentManager.beginTransaction()
            .add(R.id.base_frame_layout, fragment)
            .addToBackStack(fragment.tag ?: "")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    fun triggerRebirth() {
        val i = Intent(baseContext, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        baseContext.startActivity(i)
    }
}