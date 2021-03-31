package com.example.myapplication.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.callbacks.SpecificItemCallback
import com.example.myapplication.data.default
import com.example.myapplication.debugToaster.ToastType
import com.example.myapplication.models.InfoDataHolder
import com.example.myapplication.ui.InfoListTabAdapter
import com.example.myapplication.utils.getInfoIcon
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val LIST_ARG = "info list"

class InfoOverviewFragment: Fragment(R.layout.screen_info_overview), SpecificItemCallback {

    companion object {
        val searchContentLiveData = MutableLiveData<String>().default("")
    }

    private lateinit var adapter: InfoListTabAdapter
    private lateinit var viewPager: ViewPager2

    private lateinit var searchEditText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(LIST_ARG) }?.apply {
            //val textView: TextView = view.findViewById(android.R.id.text1)
            //textView.text = getInt(ARG_OBJECT).toString()
        }

        initViews(view)
        setSearchLogic()

        adapter = InfoListTabAdapter(this, this)
        viewPager.adapter = adapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            //tab.text = "OBJECT ${(position + 1)}"
            tab.text = ToastType.values()[position].name
            tab.icon = getInfoIcon(context!!, position)
        }.attach()
    }

    private fun initViews(view: View) {
        searchEditText = view.findViewById(R.id.search_edit_text)
        viewPager = view.findViewById(R.id.pager)
    }

    private fun setSearchLogic() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchContentLiveData.postValue(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    //-------------------- SpecificItemCallback Implementation --------------------

    override fun onSpecificItemPressed(item: InfoDataHolder) {
        val fragment = InfoDetailedViewFragment(item)
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.contentContainer, fragment)
            addToBackStack(null)
            commit()
        }
    }
}