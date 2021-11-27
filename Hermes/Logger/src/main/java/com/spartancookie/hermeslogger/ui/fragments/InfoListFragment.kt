package com.spartancookie.hermeslogger.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.callbacks.LogSelectedCallback
import com.spartancookie.hermeslogger.core.HermesHandler
import com.spartancookie.hermeslogger.core.LogType
import com.spartancookie.hermeslogger.models.LogDataHolder
import com.spartancookie.hermeslogger.models.filterLogs
import com.spartancookie.hermeslogger.ui.adapters.InfoRecyclerAdapter
import com.spartancookie.hermeslogger.ui.search.CustomSearch

private const val LOG_TYPE_ARG = "LogTypeArg"
private const val LOG_SELECTED_CALLBACK_ARG = "LogSelectedCallback"

internal class InfoListFragment : Fragment(R.layout.screen_info_list) {

    private lateinit var logSelectedCallback: LogSelectedCallback
    private var type: LogType? = null

    private var filterString = EMPTY_STRING
    private var matchCase = false

    private var storedLogList: List<LogDataHolder> = mutableListOf()

    private var customSearch = CustomSearch()
    private var nrOfLogs = 0

    private val infoHolder get() = HermesHandler.infoHolder
    private val logList: List<LogDataHolder>
        get() = type?.let { infoHolder.getLogListByType(it).reversed() }
            ?: infoHolder.logList.reversed()

    private val mAdapter: InfoRecyclerAdapter by lazy { InfoRecyclerAdapter(logList.toMutableList(), requireActivity(), logSelectedCallback, this) }
    private val mLayoutManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(requireActivity()) }
    private val recyclerView: RecyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.recycler) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Get type from bundle
        arguments?.run {
            type = getSerializable(LOG_TYPE_ARG) as? LogType
            logSelectedCallback = getSerializable(LOG_SELECTED_CALLBACK_ARG) as LogSelectedCallback
        }

        activity?.let {
            val dividerDecor = DividerItemDecoration(
                it.applicationContext,
                (mLayoutManager as LinearLayoutManager).orientation
            )

            with(recyclerView) {
                layoutManager = mLayoutManager
                adapter = mAdapter
                addItemDecoration(dividerDecor)
            }
        }
        setObservers()
    }

    private fun setObservers() {

        // Subscribe infoLiveData, to receive update when new logs are added or removed
        infoHolder.infoLiveData.observe(viewLifecycleOwner, Observer {
            if (nrOfLogs != logList.size) {
                // Get the list of logs filtered by the search content
                storedLogList = logList.filterLogs(customSearch)

                val wereItemsRemoved = nrOfLogs > logList.size
                updateList(wereItemsRemoved)

                nrOfLogs = logList.size
            }
        })

        // Subscribe customSearchLiveData, to receive update when search content changes
        InfoOverviewFragment.customSearchLiveData.observe(viewLifecycleOwner, Observer {
            customSearch = it
            val changedCase = customSearch.matchCase && matchCase
            val changedContent = customSearch.filterContent.contains(filterString)
            val canUsePreviousFilter =
                customSearch.filterContent.isNotEmpty() && changedContent && changedCase

            filterString = customSearch.filterContent
            matchCase = customSearch.matchCase

            storedLogList =
                (if (canUsePreviousFilter) storedLogList else logList).filterLogs(customSearch)
            mAdapter.updateList(storedLogList)
        })

    }

    //------------------------- Helper methods -------------------------

    private fun updateList(wereItemsRemoved: Boolean) {
        if (!recyclerView.canScrollVertically(-1) || wereItemsRemoved) {
            // If the user is all the way scrolled up, notify the adapter with notifyDataSetChanged()
            mAdapter.updateList(storedLogList)
        } else {
            // If the user is not scrolled all the way up, notify the adapter with notifyItemRangeInserted(...), to keep the scroll position
            mAdapter.updateListOnTop(storedLogList, logList.size - nrOfLogs)
        }
    }

    //------------------------------ Factory ------------------------------

    companion object {

        /**
         * Create an instance of [InfoListFragment] with a bundle that contains the [LogType]
         * selected and with an implementation of [LogSelectedCallback].
         * @param type [LogType] from the tab selected.
         * @param logSelectedCallback implementation from the parent fragment.
         */
        fun newInstance(type: LogType?, logSelectedCallback: LogSelectedCallback) =
            InfoListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(LOG_TYPE_ARG, type)
                    putSerializable(LOG_SELECTED_CALLBACK_ARG, logSelectedCallback)
                }
            }
    }
}