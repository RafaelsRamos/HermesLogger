package com.example.myapplication.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.R
import com.example.myapplication.ui.fragments.InfoOverviewFragment
import kotlinx.android.synthetic.main.screen_overview_background.view.*

class OverviewLayout: ConstraintLayout {

    private var ctx: FragmentActivity? = null
    private var childView: View? = null

    private var isOverviewOpen = false
    private var isSettingsOpen = false

    //------------------- Initialization -------------------

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        if(context is FragmentActivity) {
            ctx = context as FragmentActivity
        }
        childView = inflate(context, R.layout.screen_overview_background, this)
        loadOverview()
        setListeners()
    }

    private fun setListeners() {
        close.setOnClickListener { close() }
        infoOverviewTab.setOnClickListener { openOverview() }
        infoSettingsTab.setOnClickListener { openSettings() }
    }

    private fun loadOverview() {
        loadFragment(InfoOverviewFragment())
        insideLayout.visibility = View.VISIBLE
        infoOverviewTab.background = ContextCompat.getDrawable(context, R.drawable.half_circle_pressed)
    }

    private fun loadSettings() {
        loadFragment(InfoOverviewFragment())
        insideLayout.visibility = View.VISIBLE
    }

    //--------------------------- Commands ---------------------------

    private fun close() {
        this.removeView(childView)
        insideLayout.visibility = View.GONE
        setTabsState(overview = false,settings = false)
    }

    private fun openOverview() {
        if (ctx != null && !isOverviewOpen) {
            loadOverview()
            setTabsState(overview = true,settings = false)
        }
    }

    private fun openSettings() {
        if (ctx != null && !isSettingsOpen) {
            loadSettings()
            setTabsState(overview = false,settings = true)
        }
    }

    //--------------------- Helper methods ---------------------

    private fun loadFragment(fragment: Fragment) {
        val transaction = ctx!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.runtimeInfoContentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setTabsState(overview: Boolean, settings: Boolean) {
        this.isOverviewOpen = overview
        this.isSettingsOpen = settings
        infoOverviewTab.background = getTabDrawable(isOverviewOpen)
        infoSettingsTab.background = getTabDrawable(isSettingsOpen)
    }

    private fun getTabDrawable(state: Boolean): Drawable? {
        return ContextCompat.getDrawable(context, if (state) R.drawable.half_circle_pressed else R.drawable.half_circle_unpressed)
    }
}