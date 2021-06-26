package com.spartancookie.hermeslogger.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.callbacks.SpecificItemCallback
import com.spartancookie.hermeslogger.debugToaster.LogType
import com.spartancookie.hermeslogger.debugToaster.Toaster
import com.spartancookie.hermeslogger.models.LogDataHolder
import com.spartancookie.hermeslogger.models.filterLogs
import com.spartancookie.hermeslogger.ui.adapters.InfoRecyclerAdapter
import com.spartancookie.hermeslogger.ui.search.CustomSearch

private const val LOG_TYPE_ARG = "LogTypeArg"

internal class InfoListFragment : Fragment(R.layout.screen_info_list) {

    private lateinit var specificItemCallback: SpecificItemCallback
    private var type: LogType? = null
    private var filterString = EMPTY_STRING

    private var storedLogList: List<LogDataHolder> = mutableListOf()

    private var customSearch = CustomSearch()
    private var nrOfLogs = 0

    private val infoHolder get() = Toaster.instance.infoHolder
    private val logList: List<LogDataHolder>
        get() = type?.let { infoHolder.getLogListByType(it).reversed() } ?: infoHolder.logList.reversed()

    private val mAdapter: InfoRecyclerAdapter by lazy { InfoRecyclerAdapter(logList.toMutableList(), activity!!, specificItemCallback, this) }
    private val mLayoutManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(activity!!) }
    private val recyclerView: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.recycler) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Get type from bundle
        type = arguments?.getSerializable(LOG_TYPE_ARG) as? LogType

        activity?.let {
            val dividerDecor = DividerItemDecoration(it.applicationContext, (mLayoutManager as LinearLayoutManager).orientation)

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
                storedLogList = logList.filterLogs(customSearch)

                if (!recyclerView.canScrollVertically(-1)) {
                    mAdapter.updateList(storedLogList)
                } else {
                    mAdapter.updateListOnTop(storedLogList, logList.size - nrOfLogs)
                }

                nrOfLogs = logList.size
            }
        })

        InfoOverviewFragment.customSearchLiveData.observe(this, Observer {
            customSearch = it
            val canUsePreviousFilter = customSearch.filterContent.isNotEmpty() && customSearch.filterContent.contains(filterString)
            filterString = customSearch.filterContent
            storedLogList = (if (canUsePreviousFilter) storedLogList else logList).filterLogs(customSearch)
            mAdapter.updateList(storedLogList)
        })

    }

    //------------------------------ Factory ------------------------------

    companion object {
        fun newInstance(type: LogType?, itemCallback: SpecificItemCallback): InfoListFragment {
            val args = Bundle().apply { putSerializable(LOG_TYPE_ARG, type) }
            return InfoListFragment().apply {
                arguments = args
                specificItemCallback = itemCallback
            }
        }
    }
}