package com.spartancookie.hermeslogger.filters.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.spartancookie.hermeslogger.GhostFragment
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.core.HermesHandler
import com.spartancookie.hermeslogger.filters.FilterManager
import com.spartancookie.hermeslogger.filters.models.FilterByTag
import com.spartancookie.hermeslogger.filters.models.FilterByType
import com.spartancookie.hermeslogger.filters.ui.adapters.FiltersAdapter
import com.spartancookie.hermeslogger.ui.decorators.MarginItemDecoration
import kotlinx.android.synthetic.main.hermes_screen_filters.*

@GhostFragment
internal class FiltersFragment: Fragment(R.layout.hermes_screen_filters) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var filters = mutableListOf<Any>().apply {
            add("Event types")
            addAll(fetchTypeFilters())

            if (FilterManager.tagFilters.isNotEmpty()) {
                add("Active tags")
                addAll(FilterManager.tagFilters)
            }

            add("More...")
            val availableTags = HermesHandler.infoHolder.getTagsList()
            addAll(availableTags.map { FilterByTag(it) })
        }.distinct()

        if (filters.last() is String) {
            filters = filters.subList(0, filters.lastIndex)
        }

        with (filters_recycler) {
            adapter = FiltersAdapter(filters)
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            addItemDecoration(MarginItemDecoration.withSameMargins(12))
        }
    }

    private fun fetchTypeFilters(): List<FilterByType> = listOf(
        FilterByType(EventType.Success),
        FilterByType(EventType.Verbose),
        FilterByType(EventType.Info),
        FilterByType(EventType.Debug),
        FilterByType(EventType.Warning),
        FilterByType(EventType.Error),
        FilterByType(EventType.Wtf),
    )

    companion object {
        fun newInstance() = FiltersFragment()
    }

}