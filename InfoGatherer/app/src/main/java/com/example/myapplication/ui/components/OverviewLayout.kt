package com.example.myapplication.ui.components

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
import com.example.myapplication.R
import com.example.myapplication.ui.fragments.InfoOverviewFragment
import kotlinx.android.synthetic.main.screen_overview_background.view.*

class OverviewLayout @JvmOverloads private constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var childView: View? = null

    private var isOverviewVisible = false

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
        childView = inflate(context, R.layout.screen_overview_background, this)
        loadOverview()
        setListeners()
    }

    private fun setListeners() {
        close.setOnClickListener { close() }
        infoOverviewTab.setOnClickListener { openOverview() }
    }

    private fun loadOverview() {
        loadFragment(InfoOverviewFragment())
        insideLayout.visibility = View.VISIBLE
        infoOverviewTab.background = ContextCompat.getDrawable(context, R.drawable.half_circle_pressed)
    }

    //--------------------------- Commands ---------------------------

    private fun close() {
        this.removeView(childView)
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
        return ContextCompat.getDrawable(context, if (state) R.drawable.half_circle_pressed else R.drawable.half_circle_unpressed)
    }
}