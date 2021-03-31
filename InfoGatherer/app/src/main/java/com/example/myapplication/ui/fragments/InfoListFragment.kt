package com.example.myapplication.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.callbacks.SpecificItemCallback
import com.example.myapplication.debugToaster.ToastType
import com.example.myapplication.debugToaster.Toaster
import com.example.myapplication.models.InfoDataHolder
import com.example.myapplication.ui.adapters.InfoRecyclerAdapter


class InfoListFragment(private val type: ToastType,private val specificItemCallback: SpecificItemCallback): Fragment(R.layout.screen_info_list),
    SpecificItemCallback {

    private val logList: List<InfoDataHolder>?
        get() = Toaster.instance?.infoHolder?.getInfoByType(type)

    var storedLogList: List<InfoDataHolder> = mutableListOf()

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InfoRecyclerAdapter

    private var filterString = ""
    private var nrOfLogs = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (logList != null && activity != null) {
            recyclerView = view.findViewById(R.id.recycler)
            layoutManager = LinearLayoutManager(activity)
            recyclerView.layoutManager = layoutManager
            val mDividerItemDecoration = DividerItemDecoration(
                recyclerView.context,
                (layoutManager as LinearLayoutManager).orientation
            )
            recyclerView.addItemDecoration(mDividerItemDecoration)

            adapter = InfoRecyclerAdapter(logList!!, activity!!, specificItemCallback)
            recyclerView.adapter= adapter
        }

        setObservers()
    }

    private fun setObservers() {

        Toaster.instance?.infoHolder?.infoLiveData?.observe(this, Observer {
            if (nrOfLogs != logList?.size) {
                //adapter.updateList(logList!!)
                getFilteredLogs(logList!!)
            }
        })

        InfoOverviewFragment.searchContentLiveData.observe(this, Observer {
            val canUsePreviousFilter = filterString.contains(it)
            filterString = it

            getFilteredLogs(if (canUsePreviousFilter) storedLogList else logList!!)

            val filteredList = logList?.filter { dataHolder ->
                dataHolder.creationDate.toString().contains(it) || dataHolder.extraInfo?.contains(it) ?: false || dataHolder.msg.contains(it)
            }
            filteredList?.let { validList -> adapter.updateList(validList) }
        })

    }

    private fun getFilteredLogs(infoList: List<InfoDataHolder>) {
        val filteredList = infoList.filter { dataHolder ->
            dataHolder.creationDate.toString().contains(filterString) || dataHolder.extraInfo?.contains(filterString) ?: false || dataHolder.msg.contains(filterString)
        }
        filteredList.let { validList -> adapter.updateList(validList) }
    }

    override fun onSpecificItemPressed(item: InfoDataHolder) {
        TODO("Not yet implemented")
    }
}