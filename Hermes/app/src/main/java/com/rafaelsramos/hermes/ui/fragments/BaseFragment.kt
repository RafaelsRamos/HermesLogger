package com.rafaelsramos.hermes.ui.fragments

import androidx.fragment.app.Fragment
import com.rafaelsramos.hermes.ICommunicationChannel

abstract class BaseFragment(layoutRes: Int): Fragment(layoutRes) {

    var communicationChannel: ICommunicationChannel? = null

    protected fun loadFragment(fragment: BaseFragment) = communicationChannel?.loadFragment(fragment)
}