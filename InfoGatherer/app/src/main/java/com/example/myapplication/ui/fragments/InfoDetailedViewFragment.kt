package com.example.myapplication.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.models.LogDataHolder
import com.example.myapplication.utils.DateFormat
import com.example.myapplication.utils.getInfoIcon
import java.text.SimpleDateFormat

class InfoDetailedViewFragment(private val item: LogDataHolder) : Fragment(R.layout.screen_detailed_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val format = SimpleDateFormat(DateFormat)

        view.findViewById<TextView>(R.id.date).text = format.format(item.creationDate.time)
        view.findViewById<TextView>(R.id.message).text = item.msg
        view.findViewById<TextView>(R.id.extraInfo).text = item.extraInfo
        view.findViewById<ImageView>(R.id.type_icon).setImageDrawable(getInfoIcon(context!!, item.type.ordinal))
        view.findViewById<TextView>(R.id.type_name).text = item.type.name
        view.findViewById<View>(R.id.back).setOnClickListener { activity?.onBackPressed() }
    }
}