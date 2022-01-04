package com.spartancookie.hermeslogger.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.callbacks.EventSelectedCallback
import com.spartancookie.hermeslogger.core.models.EventDataHolder
import com.spartancookie.hermeslogger.utils.DateFormat
import java.text.SimpleDateFormat

internal class InfoRecyclerAdapter(private var eventList: MutableList<EventDataHolder>, private val callback: EventSelectedCallback, private val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<InfoRecyclerAdapter.InfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_event_recycler, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    override fun getItemCount() = eventList.size

    fun updateList(eventList: List<EventDataHolder>) {
        updateAllItems(eventList)
        notifyDataSetChanged()
    }

    fun updateListOnTop(eventList: List<EventDataHolder>, nrOfItemsAdded: Int) {
        updateAllItems(eventList)
        notifyItemRangeInserted(0, nrOfItemsAdded)
    }

    fun removeItemInPosition(pos: Int) {
        this.eventList.removeAt(pos)
        notifyDataSetChanged()
    }

    //---------------- Helper methods ----------------

    private fun updateAllItems(eventList: List<EventDataHolder>) {
        this.eventList.clear()
        this.eventList.addAll(eventList)
    }

    //----------------- View Holder -----------------

    inner class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.icon)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val date: TextView = itemView.findViewById(R.id.date)
        private val expandOrCollapse: ImageView = itemView.findViewById(R.id.expand_collapse_iv)

        private val extraInfoSection: ViewGroup = itemView.findViewById(R.id.extra_info_layout)
        private val description: TextView = extraInfoSection.findViewById(R.id.description)

        val id: TextView = itemView.findViewById(R.id.log_id_tv)

        fun bind(event: EventDataHolder) {
            val format = SimpleDateFormat(DateFormat)

            val context = itemView.context

            // Set collapsed information
            title.text = event.message
            date.text = format.format(event.creationDate.time)
            id.text = event.id
            icon.setImageDrawable(ContextCompat.getDrawable(context, event.type.drawableResource))

            // Set expanded extra information
            description.text = event.description

            expandOrCollapse.setOnClickListener {
                val isExtraInfoVisible = extraInfoSection.visibility == View.VISIBLE
                if (isExtraInfoVisible) {
                    expandOrCollapse.setImageResource(R.drawable.hermes_logger_arrow_down)
                    extraInfoSection.visibility = View.GONE
                } else {
                    expandOrCollapse.setImageResource(R.drawable.hermes_logger_arrow_up)
                    extraInfoSection.visibility = View.VISIBLE
                }
            }
            itemView.setOnClickListener { callback.onLogSelected(event) }
        }
    }
}