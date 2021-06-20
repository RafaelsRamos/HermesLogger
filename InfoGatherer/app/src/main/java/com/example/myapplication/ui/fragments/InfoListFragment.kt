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

    private val infoHolder get() = Toaster.instance.infoHolder

    private val logList: List<LogDataHolder>
        get() = type?.let { infoHolder.getLogListByType(type).reversed() } ?: infoHolder.logList.reversed()

    var storedLogList: List<LogDataHolder> = mutableListOf()

    private lateinit var mAdapter: InfoRecyclerAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerView: RecyclerView

    private var filterString = ""
    private var nrOfLogs = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.let {
            mLayoutManager = LinearLayoutManager(it)
            mAdapter = InfoRecyclerAdapter(logList, it, specificItemCallback)
            val dividerDecor = DividerItemDecoration(it.applicationContext, (mLayoutManager as LinearLayoutManager).orientation)

            recyclerView = view.findViewById(R.id.recycler)
            with (recyclerView) {
                layoutManager = mLayoutManager
                adapter = mAdapter
                addItemDecoration(dividerDecor)
            }
        }
        setObservers()
    }

    private fun setObservers() {

        infoHolder.infoLiveData.observe(this, Observer {
            if (nrOfLogs != logList.size) {
                storedLogList = getFilteredLogs(logList)

                if (!recyclerView.canScrollVertically(-1)) {
                    mAdapter.updateList(storedLogList)
                } else {
                    mAdapter.updateListOnTop(storedLogList, logList.size - nrOfLogs)
                }

                nrOfLogs = logList.size
            }
        })

        InfoOverviewFragment.searchContentLiveData.observe(this, Observer {
            val canUsePreviousFilter = it.isNotEmpty() && filterString.contains(it)
            filterString = it
            storedLogList = getFilteredLogs(if (canUsePreviousFilter) storedLogList else logList)
            mAdapter.updateList(storedLogList)
        })

    }

    private fun getFilteredLogs(logList: List<LogDataHolder>): List<LogDataHolder> {
        return logList.filter { dataHolder ->
            dataHolder.creationDate.toString().contains(filterString) ||
            dataHolder.extraInfo?.contains(filterString) ?: false ||
            dataHolder.message.contains(filterString) ||
            dataHolder.id.contains(filterString)
        }
    }
}