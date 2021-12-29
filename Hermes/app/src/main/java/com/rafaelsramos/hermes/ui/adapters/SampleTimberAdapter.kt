package com.rafaelsramos.hermes.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rafaelsramos.hermes.R
import com.rafaelsramos.hermes.utils.toHTag
import timber.log.Timber

class SampleTimberAdapter(private val items: List<String>): RecyclerView.Adapter<SampleTimberAdapter.ViewHolder>() {

    init {
        Timber.d("Displaying ${items.size} items")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflated = LayoutInflater.from(parent.context)
        val view = inflated.inflate(R.layout.item_sample_timber, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size

    class ViewHolder(private val root: View): RecyclerView.ViewHolder(root) {
        private val titleBtn = root.findViewById<Button>(R.id.title)

        fun bind(title: String, position: Int) {
            titleBtn.text = title
            titleBtn.setOnClickListener {
                Toast.makeText(root.context, "clicked #$position", Toast.LENGTH_SHORT).show()

                // With App's String to Tag
                Timber.d("${"User-Click".toHTag()}Pressed item #$position")
                // With Hermes Wrapper
                //Timber.hermesTag("User-Click").d("Pressed item #$position")
            }
        }

    }
}