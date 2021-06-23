package com.example.myapplication.ui.components

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.R
import com.example.myapplication.callbacks.FragmentCommunicator
import com.example.myapplication.managers.OverviewStateHolderUpdater
import com.example.myapplication.ui.fragments.InfoOverviewFragment


class OverviewLayout private constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), FragmentCommunicator {

    private var childView: View = inflate(context, R.layout.screen_overview_background, this)

    private var isOverviewVisible = false

    private val stateHolderInstance = OverviewStateHolderUpdater()

    private val insideLayout by lazy { childView.findViewById<ConstraintLayout>(R.id.insideLayout) }
    private val infoOverviewTab by lazy { childView.findViewById<RelativeLayout>(R.id.infoOverviewTab) }
    private val close by lazy { childView.findViewById<RelativeLayout>(R.id.close) }
    private val background by lazy { childView.findViewById<View>(R.id.background) }

    private val fragmentActivity get() = context as? FragmentActivity

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
    }

    private fun loadOverview() {
        val overviewFragment = InfoOverviewFragment().apply {
            stateHolder = stateHolderInstance
            communicator = this@OverviewLayout as FragmentCommunicator
        }

        loadFragment(overviewFragment)
        insideLayout.visibility = View.VISIBLE
        background.visibility = View.VISIBLE
        infoOverviewTab.background = ContextCompat.getDrawable(
            context,
            R.drawable.half_circle_pressed
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
            if (state) R.drawable.half_circle_pressed else R.drawable.half_circle_unpressed
        )
    }

    //----------- FragmentCommunicator implementation -----------

    /**
     * Method triggered on InfoOverviewFragment detached
     */
    override fun onFragmentDetached() {
        close()
    }
}