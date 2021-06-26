package com.rafaelsramos.hermes.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rafaelsramos.hermes.callbacks.SpecificItemCallback
import com.rafaelsramos.hermes.debugToaster.LogType
import com.rafaelsramos.hermes.ui.fragments.InfoListFragment

internal class InfoListTabAdapter(fragment: Fragment, private val specificItemCallback: SpecificItemCallback) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 6

    override fun createFragment(position: Int): Fragment {
        return InfoListFragment.newInstance(if (position == 0) null else LogType.values()[position - 1], specificItemCallback)
    }
}