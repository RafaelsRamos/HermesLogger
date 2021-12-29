package com.spartancookie.hermeslogger.ui.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spartancookie.hermeslogger.GhostFragment
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.callbacks.LogSelectedCallback
import com.spartancookie.hermeslogger.core.HermesHandler
import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.filters.applyFilters
import com.spartancookie.hermeslogger.models.EventDataHolder
import com.spartancookie.hermeslogger.models.filterLogs
import com.spartancookie.hermeslogger.ui.adapters.InfoRecyclerAdapter
import com.spartancookie.hermeslogger.ui.search.CustomSearch
import com.spartancookie.hermeslogger.ui.search.contains
import com.spartancookie.hermeslogger.utils.EMPTY_STRING

private const val LOG_SELECTED_CALLBACK_ARG = "LogSelectedCallback"

@GhostFragment
internal class InfoListFragment : Fragment(R.layout.screen_info_list) {

    private lateinit var logSelectedCallback: LogSelectedCallback
    private var type: EventType? = null

    private var filterString = EMPTY_STRING
    private var matchCase = false

    private val storedEventList: MutableList<EventDataHolder> = mutableListOf()

    private var customSearch = CustomSearch()

    private val infoHolder get() = HermesHandler.infoHolder
    private val eventList: List<EventDataHolder>
        get() = infoHolder.eventList.sortedBy { it.creationDate }.reversed()

    private lateinit var mAdapter: InfoRecyclerAdapter

    private val mLayoutManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(requireActivity()) }
    private val recyclerView: RecyclerView by lazy { requireView().findViewById(R.id.recycler) }

    private var isSwiping = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Get type from bundle
        arguments?.run {
            logSelectedCallback = getSerializable(LOG_SELECTED_CALLBACK_ARG) as LogSelectedCallback
        }

        val dividerDecor = DividerItemDecoration(context, (mLayoutManager as LinearLayoutManager).orientation)
        mAdapter = InfoRecyclerAdapter(eventList.toMutableList(), logSelectedCallback, this)


        with(recyclerView) {
            layoutManager = mLayoutManager
            adapter = mAdapter
            ItemTouchHelper(itemTouchHelperCallbackImpl()).attachToRecyclerView(this)
            addItemDecoration(dividerDecor)
        }

        setObservers()
    }

    private fun setObservers() {
        // Subscribe eventsReplacedLiveData, to receive update when logs are removed
        HermesHandler.infoHolder.eventsReplacedLiveData.observe(viewLifecycleOwner, { newEventList ->
            overrideEventList(newEventList)
        })

        // Subscribe eventAddedLiveData, to add events when they're added
        HermesHandler.infoHolder.eventAddedLiveData.observe(viewLifecycleOwner, { event ->
            // If the new event is not removed by the filters set or by the Search content, add it
            if (event.applyFilters().isNotEmpty() && event.contains(customSearch)) {
                storedEventList.add(0, event)
                updateList()
            }
        })

        // Subscribe customSearchLiveData, to receive update when search content changes
        InfoOverviewFragment.customSearchLiveData.observe(viewLifecycleOwner, {
            customSearch = it
            val changedCase = customSearch.matchCase && matchCase
            val changedContent = customSearch.filterContent.contains(filterString)
            val canUsePreviousFilter = customSearch.filterContent.isNotEmpty() && changedContent && changedCase

            filterString = customSearch.filterContent
            matchCase = customSearch.matchCase

            val events = if (canUsePreviousFilter) storedEventList else eventList
            overrideEventList(events.filterLogs(customSearch))
        })

    }

    //------------------------- Helper methods -------------------------

    private fun updateList() {
        if (!recyclerView.canScrollVertically(-1) && !isSwiping) {
            // If the user is all the way scrolled up, notify the adapter with notifyDataSetChanged()
            mAdapter.updateList(storedEventList)
        } else {
            // If the user is not scrolled all the way up, notify the adapter with notifyItemRangeInserted(...), to keep the scroll position
            mAdapter.updateListOnTop(storedEventList, 1)
        }
    }

    private fun overrideEventList(events: Collection<EventDataHolder>) {
        storedEventList.clear()
        storedEventList.addAll(events)
        mAdapter.updateList(storedEventList)
    }

    private fun itemTouchHelperCallbackImpl() = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            isSwiping = false

            val event = storedEventList[viewHolder.adapterPosition]
            infoHolder.removeLogById(event.id)

            storedEventList.removeAt(viewHolder.adapterPosition)
            mAdapter.updateList(storedEventList)
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                isSwiping = true
            }
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