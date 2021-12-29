package com.spartancookie.hermeslogger.ui.components

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.callbacks.FragmentStateCallback
import com.spartancookie.hermeslogger.core.HermesConfigurations
import com.spartancookie.hermeslogger.core.HermesHandler
import com.spartancookie.hermeslogger.filters.FilterManager
import com.spartancookie.hermeslogger.share.ShareHelperCommon
import com.spartancookie.hermeslogger.share.ShareHelperCommon.shareWholeLogStack
import com.spartancookie.hermeslogger.ui.fragments.FiltersFragment
import com.spartancookie.hermeslogger.ui.fragments.InfoOverviewFragment
import com.spartancookie.hermeslogger.utils.canShareHermesLogDumps
import com.spartancookie.hermeslogger.utils.clearAllFragments
import kotlinx.android.synthetic.main.include_top_options.view.*
import kotlinx.android.synthetic.main.screen_overview_background.view.*
import kotlinx.android.synthetic.main.screen_overview_background.view.export_image_view
import kotlinx.android.synthetic.main.screen_overview_background.view.remove_image_view


class OverviewLayout private constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr), FragmentStateCallback {

    private var filteringFragment = false

    private val fragmentActivity get() = context as? FragmentActivity
    private val fragmentManager get() = fragmentActivity?.supportFragmentManager

    private val hermesHandler get() = HermesHandler
    private val infoHolder get() = hermesHandler.infoHolder

    private val isOverviewVisible get() = insideLayout.visibility == View.VISIBLE

    //-------------------------- Factory --------------------------

    companion object {

        /**
         * Create and add an instance of [OverviewLayout] onto the activity's base ViewGroup
         * @param activity Activity reference to inflate to view into
         * @return OverViewLayout that was inflated onto the activity's base ViewGroup
         */
        @JvmStatic
        fun create(activity: Activity) {

            // If Hermes is disabled, do not attach Overview
            if (!HermesConfigurations.isEnabled) return

            val container = activity.findViewById<ViewGroup>(android.R.id.content)

            // Try fetching the layout from the activity's root
            val existingOverviewLayout =
                container.findViewById<View>(R.id.parent_overview_cl_homer_logger)?.parent as? OverviewLayout

            // If there is no OverviewLayout already inflated, inflate it
            if (existingOverviewLayout == null) {
                container.addView(OverviewLayout(activity))
            }

            // Initialize filters
            FilterManager.initialize(activity)
        }
    }

    //------------------- Initialization -------------------

    init {
        inflate(context, R.layout.screen_overview_background, this)

        // Initialize Share
        ShareHelperCommon.enableShareFeature(context)

        setListeners()
    }

    private fun setListeners() {

        close.setOnClickListener { close() }
        overviewBackground.setOnClickListener { close() }

        infoOverviewTab.setOnClickListener { openOverview() }

        export_image_view.run {
            setOnClickListener {
                if (canShareHermesLogDumps(context)) {
                    shareWholeLogStack(context)
                }
            }
        }

        remove_image_view.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete all events")
                .setMessage("Are you sure you want to delete all events?")
                .setPositiveButton("Yes") { _, _ -> infoHolder.clearAllLogs() }
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }

        filter_image_view.setOnClickListener {
            filteringFragment = !filteringFragment
            if (filteringFragment) {
                loadFragment(FiltersFragment.newInstance())
            } else {
                clearFrameLayout()
                loadOverview()
            }
        }
    }

    private fun loadOverview() {
        val overviewFragment = InfoOverviewFragment.newInstance(this)

        export_image_view.visibility = if (canShareHermesLogDumps(context)) VISIBLE else GONE

        loadFragment(overviewFragment)
        insideLayout.visibility = View.VISIBLE
        overviewBackground.visibility = View.VISIBLE
        infoOverviewTab.background = ContextCompat.getDrawable(
            context,
            R.drawable.hermes_logger_half_circle_pressed
        )
    }

    //--------------------------- Commands ---------------------------

    private fun close() {
        clearFrameLayout()
        filteringFragment = false

        overviewBackground.visibility = View.GONE
        insideLayout.visibility = View.GONE
        setTabsState(show = false)
    }

    private fun openOverview() {
        fragmentActivity?.run {
            if (!isOverviewVisible) {
                loadOverview()
                setTabsState(show = true)
            }
        }
    }

    //--------------------- Helper methods ---------------------

    private fun clearFrameLayout() {
        fragmentManager?.clearAllFragments()
    }

    private fun loadFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()?.run {
            add(R.id.runtimeInfoContentContainer, fragment, InfoOverviewFragment.TAG)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            commitNowAllowingStateLoss()
        }
    }

    private fun setTabsState(show: Boolean) {
        infoOverviewTab.background = getTabDrawable(show)
    }

    private fun getTabDrawable(state: Boolean): Drawable? {
        return ContextCompat.getDrawable(
            context,
            if (state) R.drawable.hermes_logger_half_circle_pressed else R.drawable.hermes_logger_half_circle_unpressed
        )
    }

    //--------------------- FragmentStateCallback implementation ---------------------

    override fun onFragmentDismissed() {
        close()
    }
}