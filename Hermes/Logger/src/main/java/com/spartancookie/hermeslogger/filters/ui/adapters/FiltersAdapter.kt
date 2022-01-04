package com.spartancookie.hermeslogger.filters.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.filters.Filter
import com.spartancookie.hermeslogger.filters.ui.viewholders.BaseViewHolder
import com.spartancookie.hermeslogger.filters.ui.viewholders.FiltersSectionTitleViewHolder
import com.spartancookie.hermeslogger.filters.ui.viewholders.FiltersViewHolder

/**
 * Subclass of RecyclerView.Adapter for Filters to be shown in groups. [filters] have to
 * be of type [String] or a subclass of [Filter], in order to be shown.
 */
class FiltersAdapter(private val filters: List<*>): RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)

        val type = Type.from(viewType)
        val view = layoutInflater.inflate(type.layoutRes, parent, false)
        return type.fetchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item = filters[position]

        when {
            // In case the item is a filter
            item is Filter && holder is FiltersViewHolder -> holder.bind(item)

            // In case the item is the section string
            item is String && holder is FiltersSectionTitleViewHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    override fun getItemViewType(position: Int): Int {
        return Type.getIndex(filters[position])
    }

    /**
     * Types of Item that can be displayed on [FiltersAdapter] along with the respective
     * layout - [layoutRes] and ViewHolder creation function - [fetchViewHolder]
     */
    private enum class Type(val layoutRes: Int, val fetchViewHolder: (View) -> BaseViewHolder<*>) {
        FILTER(R.layout.hermes_filter, { v -> FiltersViewHolder(v) }),
        SECTION_NAME(R.layout.hermes_filters_title, { v -> FiltersSectionTitleViewHolder(v) });

        companion object {

            fun from(index: Int) = values().firstOrNull { index == it.ordinal } ?: SECTION_NAME

            fun getIndex(obj: Any?): Int {
                val type = if (obj is Filter) FILTER else SECTION_NAME
                return type.ordinal
            }
        }
    }
}