package com.example.myapplication.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.callbacks.SpecificItemCallback
import com.example.myapplication.debugToaster.ToastType
import com.example.myapplication.ui.fragments.InfoListFragment

class InfoListTabAdapter(fragment: Fragment, private val specificItemCallback: SpecificItemCallback) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return InfoListFragment(ToastType.values()[position], specificItemCallback)
    }
}