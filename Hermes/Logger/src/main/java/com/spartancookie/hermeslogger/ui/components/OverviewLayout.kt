package com.spartancookie.hermeslogger.ui.components

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.debugToaster.Toaster
import com.spartancookie.hermeslogger.managers.OverviewStateHolderUpdater
import com.spartancookie.hermeslogger.ui.fragments.InfoOverviewFragment
import com.spartancookie.hermeslogger.utils.default


class OverviewLayout private constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var childView: View = inflate(context, R.layout.screen_overview_background, this)

    private var isOverviewVisible = false
    private var isRemoveModeEnabled = false

    private val stateHolderInstance = OverviewStateHolderUpdater()

    private val insideLayout by lazy { childView.findViewById<ConstraintLayout>(R.id.inside_layout) }
    private val infoOverviewTab by lazy { childView.findViewById<RelativeLayout>(R.id.info_overview_tab) }
    private val close by lazy { childView.findViewById<RelativeLayout>(R.id.close) }
    private val background by lazy { childView.findViewById<View>(R.id.background) }

    private val removeImageView by lazy { childView.findViewById<ImageView>(R.id.remove_image_view) }
    private val removeAllTextView by lazy { childView.findViewById<TextView>(R.id.remove_all_text_view) }

    private val fragmentActivity get() = context as? FragmentActivity

    private val toaster get() = Toaster.instance
    private val infoHolder get() = toaster.infoHolder

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
            return OverviewLayout(activity).also {container.addView(it) }
        }

        /**
         * Used to inform the items that the remove-mode was enabled or disabled
         */
        internal val removeModeLiveData = MutableLiveData<Boolean>().default(false)

    }

    //------------------- Initialization -------------------

    init {
        loadOverview()
        setListeners()
    }

    private fun setListeners() {
        close.setOnClickListener { close() }
        background.setOnClickListener { close() }
        infoOverviewTab.setOnClickListener { openOverview() }
        removeImageView.setOnClickListener {
            isRemoveModeEnabled = !isRemoveModeEnabled
            removeAllTextView.visibility = if (isRemoveModeEnabled) View.VISIBLE else View.GONE
            removeModeLiveData.postValue(isRemoveModeEnabled)
        }
        removeAllTextView.setOnClickListener {
            // Remove all logs
            infoHolder.clearAllLogs()
            // Clear toasts queue
            toaster.clearQueue()
        }
    }

    private fun loadOverview() {
        val overviewFragment = InfoOverviewFragment().apply {
            stateHolder = stateHolderInstance
            // Invoke close() on InfoOverview fragment detached
            onDismissedFunc = ::close
        }

        loadFragment(overviewFragment)
        insideLayout.visibility = View.VISIBLE
        background.visibility = View.VISIBLE
        infoOverviewTab.background = ContextCompat.getDrawable(
            context,
            R.drawable.hermes_logger_half_circle_pressed
        )
    }

    //--------------------------- Commands ---------------------------

    private fun close() {
        this.removeView(childView)
        background.visibility = View.GONE
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

    private fun loadFragment(fragment: Fragment) {
        fragmentActivity?.run {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.runtimeInfoContentContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun setTabsState(show: Boolean) {
        this.isOverviewVisible = show
        infoOverviewTab.background = getTabDrawable(isOverviewVisible)
    }

    private fun getTabDrawable(state: Boolean): Drawable? {
        return ContextCompat.getDrawable(
            context,
            if (state) R.drawable.hermes_logger_half_circle_pressed else R.drawable.hermes_logger_half_circle_unpressed
        )
    }
}