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
import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.models.EventDataHolder
import com.spartancookie.hermeslogger.models.filterLogs
import com.spartancookie.hermeslogger.ui.adapters.InfoRecyclerAdapter
import com.spartancookie.hermeslogger.ui.search.CustomSearch
import com.spartancookie.hermeslogger.utils.EMPTY_STRING

private const val LOG_SELECTED_CALLBACK_ARG = "LogSelectedCallback"

internal class InfoListFragment : Fragment(R.layout.screen_info_list) {

    private lateinit var logSelectedCallback: LogSelectedCallback
    private var type: EventType? = null

    private var filterString = EMPTY_STRING
    private var matchCase = false

    private var storedEventList: List<EventDataHolder> = mutableListOf()

    private var customSearch = CustomSearch()
    private var nrOfLogs = 0

    private val infoHolder get() = HermesHandler.infoHolder
    private val eventList: List<EventDataHolder>
        get() = infoHolder.eventList.sortedBy { it.creationDate }.reversed()

    private val mAdapter: InfoRecyclerAdapter by lazy { InfoRecyclerAdapter(eventList.toMutableList(), requireActivity(), logSelectedCallback, this) }
    private val mLayoutManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(requireActivity()) }
    private val recyclerView: RecyclerView by lazy { requireView().findViewById(R.id.recycler) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Get type from bundle
        arguments?.run {
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
        HermesHandler.infoHolder.infoLiveData.observe(viewLifecycleOwner, {
            if (nrOfLogs != eventList.size) {
                // Get the list of logs filtered by the search content
                storedEventList = eventList.filterLogs(customSearch)

                val wereItemsRemoved = nrOfLogs > eventList.size
                updateList(wereItemsRemoved)

                nrOfLogs = eventList.size
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

            storedEventList =
                (if (canUsePreviousFilter) storedEventList else eventList).filterLogs(customSearch)
            mAdapter.updateList(storedEventList)
        })

    }

    //------------------------- Helper methods -------------------------

    private fun updateList(wereItemsRemoved: Boolean) {
        if (!recyclerView.canScrollVertically(-1) || wereItemsRemoved) {
            // If the user is all the way scrolled up, notify the adapter with notifyDataSetChanged()
            mAdapter.updateList(storedEventList)
        } else {
            // If the user is not scrolled all the way up, notify the adapter with notifyItemRangeInserted(...), to keep the scroll position
            mAdapter.updateListOnTop(storedEventList, eventList.size - nrOfLogs)
        }
    }

    //------------------------------ Factory ------------------------------

    companion object {

        /**
         * Create an instance of [InfoListFragment] with a bundle that contains the [type]
         * selected and with an implementation of [LogSelectedCallback], on [logSelectedCallback].
         */
        fun newInstance(logSelectedCallback: LogSelectedCallback) =
            InfoListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(LOG_SELECTED_CALLBACK_ARG, logSelectedCallback)
                }
            }
    }
}