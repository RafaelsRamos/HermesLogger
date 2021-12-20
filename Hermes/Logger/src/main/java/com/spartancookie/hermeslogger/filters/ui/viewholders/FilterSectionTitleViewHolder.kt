package com.spartancookie.hermeslogger.filters.ui.viewholders

import android.view.View
import android.widget.TextView
import com.spartancookie.hermeslogger.R

/**
 * Subclass of [BaseFilterViewHolder], that acts as the ViewHolder for Filters' titles.
 */
class FilterSectionTitleViewHolder(root: View): BaseFilterViewHolder<String>(root) {

    private val sectionNameTV: TextView = root.findViewById(R.id.name_tv)

    override fun bind(item: String) {
        sectionNameTV.text = item
    }

}