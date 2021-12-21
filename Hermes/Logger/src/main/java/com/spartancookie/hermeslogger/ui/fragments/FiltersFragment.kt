package com.spartancookie.hermeslogger.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.filters.FilterManager
import com.spartancookie.hermeslogger.filters.models.FilterByTag
import com.spartancookie.hermeslogger.filters.models.FilterByType
import com.spartancookie.hermeslogger.filters.ui.adapters.FiltersAdapter
import com.spartancookie.hermeslogger.ui.decorators.MarginItemDecoration
import kotlinx.android.synthetic.main.hermes_screen_filters.*

internal class FiltersFragment: Fragment(R.layout.hermes_screen_filters) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val filters = mutableListOf<Any>().apply {
            add("Event types")
            addAll(fetchTypeFilters())

            add("Tags")
            addAll(FilterManager.tagFilters) // TODO("Fetch tags registered by the devs")

            buildTagFilters()?.let { tags ->
                add("Tags 1")
                addAll(tags)
            }
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

    // TODO("Fetch tags filters")
    private fun buildTagFilters(): List<FilterByTag>? {
        return listOf(
            FilterByTag("Tag1"),
            FilterByTag("Tag2"),
            FilterByTag("Large tag Tag3"),
            FilterByTag("Large tag Tag4"),
            FilterByTag("Large tag number6"),
            FilterByTag("Large tag Tag5"),
            FilterByTag("Tag4"),
            FilterByTag("Tag5"),
        )
    }

    companion object {
        const val TAG = "FiltersFragment"
        fun newInstance() = FiltersFragment()
    }

}