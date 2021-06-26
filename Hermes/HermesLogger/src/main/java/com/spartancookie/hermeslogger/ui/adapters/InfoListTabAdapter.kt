package com.spartancookie.hermeslogger.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.spartancookie.hermeslogger.callbacks.SpecificItemCallback
import com.spartancookie.hermeslogger.debugToaster.LogType
import com.spartancookie.hermeslogger.ui.fragments.InfoListFragment

internal class InfoListTabAdapter(fragment: Fragment, private val specificItemCallback: SpecificItemCallback) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 6

    override fun createFragment(position: Int): Fragment {
        return InfoListFragment.newInstance(if (position == 0) null else LogType.values()[position - 1], specificItemCallback)
    }
}