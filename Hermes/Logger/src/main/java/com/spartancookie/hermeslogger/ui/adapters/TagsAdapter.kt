package com.spartancookie.hermeslogger.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spartancookie.hermeslogger.R

/**
 * Subclass of RecyclerView.Adapter to display the [tags].
 */
internal class TagsAdapter(private val tags: List<String>): RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.hermes_tags_title, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tags[position])
    }

    override fun getItemCount(): Int = tags.size

    class ViewHolder(root: View): RecyclerView.ViewHolder(root) {

        private val nameTv: TextView = root.findViewById(R.id.name_tv)

        fun bind(name: String) {
            nameTv.text = name
        }
    }

}