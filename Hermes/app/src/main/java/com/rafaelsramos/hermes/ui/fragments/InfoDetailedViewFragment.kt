package com.rafaelsramos.hermes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rafaelsramos.hermes.R
import com.rafaelsramos.hermes.models.LogDataHolder
import com.rafaelsramos.hermes.databinding.ScreenDetailedViewBinding
import com.rafaelsramos.hermes.utils.animateCopyToClipboardColor
import com.rafaelsramos.hermes.utils.copyToClipboard

internal class InfoDetailedViewFragment(private val item: LogDataHolder) : Fragment() {

    private lateinit var binding: ScreenDetailedViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.screen_detailed_view, container, false)
        binding.logDataHolder = item
        binding.back.setOnClickListener { activity?.onBackPressed() }
        binding.copyToClipboard.setOnClickListener { view ->
            animateCopyToClipboardColor(view)
            activity?.let{ copyToClipboard(it, item) }
        }

        return binding.root
    }
}