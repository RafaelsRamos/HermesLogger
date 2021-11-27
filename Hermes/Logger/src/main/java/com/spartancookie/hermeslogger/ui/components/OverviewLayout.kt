package com.spartancookie.hermeslogger.ui.components

import android.app.Activity
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
import androidx.lifecycle.MutableLiveData
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.callbacks.FragmentStateCallback
import com.spartancookie.hermeslogger.core.HermesHandler
import com.spartancookie.hermeslogger.ui.fragments.InfoOverviewFragment
import com.spartancookie.hermeslogger.utils.*
import com.spartancookie.hermeslogger.utils.removeFromStack
import kotlinx.android.synthetic.main.screen_overview_background.view.*


class OverviewLayout private constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr), FragmentStateCallback {

    private var isRemoveModeEnabled = false

    private val fragmentActivity get() = context as? FragmentActivity
    private val fragmentManager get() = fragmentActivity?.supportFragmentManager

    private val hermesHandler get() = HermesHandler
    private val infoHolder get() = hermesHandler.infoHolder

    private val isOverviewVisible get() = insideLayout.visibility == View.VISIBLE

    //-------------------------- Factory --------------------------

    companion object {

        /**
         * Create and add an instance of [OverviewLayout] onto the activity's base ViewGroup
         * @param activity Activity reference
         * @return  OverViewLayout that was inflated onto the activity's base ViewGroup
         */
        @JvmStatic
        fun create(activity: Activity): OverviewLayout {
            val container = activity.findViewById<ViewGroup>(android.R.id.content)
            val existingOverviewLayout =
                container.findViewById<View>(R.id.parent_overview_cl_homer_logger)?.parent as? OverviewLayout
            return existingOverviewLayout ?: OverviewLayout(activity).also { container.addView(it) }
        }

        /**
         * Used to inform the items that the remove-mode was enabled or disabled
         */
        internal val removeModeLiveData = MutableLiveData<Boolean>().default(false)

    }

    //------------------- Initialization -------------------

    init {
        inflate(context, R.layout.screen_overview_background, this)
        setListeners()
    }

    private fun setListeners() {

        close.setOnClickListener { close() }
        overviewBackground.setOnClickListener { close() }

        infoOverviewTab.setOnClickListener { openOverview() }

        val shareImageView: View = findViewById(R.id.export_image_view)
        shareImageView.run {
            if (hasWriteStoragePermission(context)) {
                setOnClickListener { shareLogDump(context) }
            } else {
                visibility = GONE
            }
        }

        val removeAllTextView: View = findViewById(R.id.remove_all_text_view)
        val removeImageView: View = findViewById(R.id.remove_image_view)
        removeImageView.setOnClickListener {
            isRemoveModeEnabled = !isRemoveModeEnabled
            removeAllTextView.visibility = if (isRemoveModeEnabled) View.VISIBLE else View.GONE
            removeModeLiveData.postValue(isRemoveModeEnabled)
        }

        removeAllTextView.setOnClickListener {
            // Remove all logs
            infoHolder.clearAllLogs()
        }
    }

    private fun loadOverview() {
        val overviewFragment = InfoOverviewFragment.newInstance(this)

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
        fragmentManager?.run { removeFromStack(this, InfoOverviewFragment.TAG) }
    }

    private fun loadFragment(fragment: Fragment) {
        fragmentActivity?.let {
            it.supportFragmentManager.beginTransaction().run {
                add(R.id.runtimeInfoContentContainer, fragment, InfoOverviewFragment.TAG)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                commitNowAllowingStateLoss()
            }
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