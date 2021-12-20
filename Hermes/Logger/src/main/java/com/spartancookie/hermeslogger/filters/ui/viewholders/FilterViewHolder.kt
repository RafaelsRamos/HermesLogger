package com.spartancookie.hermeslogger.filters.ui.viewholders

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.filters.Filter
import com.spartancookie.hermeslogger.filters.FilterManager

/**
 * Subclass of [BaseFilterViewHolder], that acts as the ViewHolder for Filters.
 */
internal class FilterViewHolder(private val root: View): BaseFilterViewHolder<Filter>(root) {

    private val nameTV = root.findViewById<TextView>(R.id.name_tv)
    private val cardView = root as CardView

    override fun bind(item: Filter) {
        nameTV.text = item.name

        fillBackground(item)

        root.setOnClickListener {
            onFilterClicked(item)
            fillBackground(item)
        }
    }

    private fun fillBackground(item: Filter) {
        val color = if (FilterManager.contains(item)) {
            ContextCompat.getColor(root.context, R.color.tabBackgroundItemSelectedColor)
        } else {
            ContextCompat.getColor(root.context, R.color.tabBackgroundItemUnSelectedColor)
        }
        cardView.setCardBackgroundColor(color)
    }

    private fun onFilterClicked(item: Filter): Boolean {
        // Add or remove filter
        if (FilterManager.contains(item)) {
            FilterManager.removeFilters(item)
        } else {
            FilterManager.addFilters(item)
        }
        FilterManager.saveFilters(root.context)
        // Return the current filter state
        return FilterManager.contains(item)
    }

}