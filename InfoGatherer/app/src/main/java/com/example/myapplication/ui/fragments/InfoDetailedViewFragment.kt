package com.example.myapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ScreenDetailedViewBinding
import com.example.myapplication.models.LogDataHolder
import com.example.myapplication.utils.DateFormat
import java.text.SimpleDateFormat

class InfoDetailedViewFragment(private val item: LogDataHolder) : Fragment() {

    private lateinit var binding: ScreenDetailedViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.screen_detailed_view, container, false)
        binding.logDataHolder = item
        binding.back.setOnClickListener { activity?.onBackPressed() }

        return binding.root
    }
}