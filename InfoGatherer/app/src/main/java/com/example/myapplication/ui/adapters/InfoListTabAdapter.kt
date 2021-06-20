package com.example.myapplication.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.callbacks.SpecificItemCallback
import com.example.myapplication.debugToaster.LogType
import com.example.myapplication.ui.fragments.InfoListFragment

class InfoListTabAdapter(fragment: Fragment, private val specificItemCallback: SpecificItemCallback) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 6

    override fun createFragment(position: Int) = InfoListFragment(if (position == 0) null else LogType.values()[position - 1], specificItemCallback)
}