package com.spartancookie.hermeslogger.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.spartancookie.hermeslogger.callbacks.LogSelectedCallback
import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.ui.fragments.InfoListFragment

internal class InfoListTabAdapter(fragment: Fragment, private val logSelectedCallback: LogSelectedCallback) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 6

    override fun createFragment(position: Int): Fragment {
        return InfoListFragment.newInstance(if (position == 0) null else EventType.values()[position - 1], logSelectedCallback)
    }
}