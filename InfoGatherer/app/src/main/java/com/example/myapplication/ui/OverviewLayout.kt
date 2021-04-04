package com.example.myapplication.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.R
import com.example.myapplication.ui.fragments.InfoOverviewFragment

class OverviewLayout: ConstraintLayout {

    private var ctx: FragmentActivity? = null
    private var childView: View? = null

    //------------------- Initialization -------------------

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        if(context is FragmentActivity) {
            ctx = context as FragmentActivity
        }
        inflateView()
    }

    private fun inflateView() {
        ctx?.let {
            childView = inflate(context, R.layout.screen_overview_background, this)
            loadFragment(InfoOverviewFragment())
            findViewById<View>(R.id.close).setOnClickListener { close() }
        }
    }

    //--------------------- Helper methods ---------------------

    private fun loadFragment(fragment: Fragment) {
        val transaction = ctx!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.runtimeInfoContentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun close() {
        this.removeView(childView)
        visibility = View.GONE
    }

    private fun open() {
        inflateView()
        visibility = View.VISIBLE
    }
}