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
import com.example.myapplication.debugToaster.LogType
import com.example.myapplication.debugToaster.Toaster
import com.example.myapplication.models.LogDataHolder
import com.example.myapplication.ui.adapters.InfoRecyclerAdapter


class InfoListFragment(private val type: LogType?, private val specificItemCallback: SpecificItemCallback): Fragment(R.layout.screen_info_list) {

    private val logList: List<LogDataHolder>?
        get() = type?.let { Toaster.instance?.infoHolder?.getLogListByType(type)?.reversed() } ?:
                            Toaster.instance?.infoHolder?.logList?.reversed()

    var storedLogList: List<LogDataHolder> = mutableListOf()

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
            logList?.let {
                if (nrOfLogs != it.size) {
                    storedLogList = getFilteredLogs(it)

                    if (!recyclerView.canScrollVertically(-1)) {
                        adapter.updateList(storedLogList)
                    } else {
                        adapter.updateListOnTop(storedLogList, it.size - nrOfLogs)
                    }

                    nrOfLogs = it.size
                }
            }
        })

        InfoOverviewFragment.searchContentLiveData.observe(this, Observer {
            val canUsePreviousFilter = filterString.contains(it)
            filterString = it

            storedLogList = getFilteredLogs(if (canUsePreviousFilter) storedLogList else logList!!)

            storedLogList.let { validList -> adapter.updateList(validList) }
        })

    }

    private fun getFilteredLogs(logList: List<LogDataHolder>): List<LogDataHolder> {
        return logList.filter { dataHolder ->
            dataHolder.creationDate.toString().contains(filterString) ||
            dataHolder.extraInfo?.contains(filterString) ?: false ||
            dataHolder.msg.contains(filterString) ||
            dataHolder.id.contains(filterString)
        }
    }
}