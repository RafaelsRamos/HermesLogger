package com.spartancookie.hermeslogger.filters.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * ViewHolder class to be used by items present in [com.spartancookie.hermeslogger.ui.fragments.FiltersFragment]
 */
abstract class BaseFilterViewHolder<T>(rootView: View): RecyclerView.ViewHolder(rootView) {

    /**
     * Set the data from [item] into the item View
     */
    abstract fun bind(item: T)

}