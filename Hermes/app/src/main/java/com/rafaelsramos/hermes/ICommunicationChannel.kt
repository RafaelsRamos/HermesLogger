package com.rafaelsramos.hermes

import com.rafaelsramos.hermes.ui.fragments.BaseFragment

interface ICommunicationChannel {

    fun loadFragment(fragment: BaseFragment)

}