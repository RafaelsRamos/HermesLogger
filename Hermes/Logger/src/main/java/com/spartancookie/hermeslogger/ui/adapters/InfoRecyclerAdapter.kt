package com.spartancookie.hermeslogger.ui.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.callbacks.LogSelectedCallback
import com.spartancookie.hermeslogger.core.HermesHandler
import com.spartancookie.hermeslogger.models.EventDataHolder
import com.spartancookie.hermeslogger.ui.components.OverviewLayout
import com.spartancookie.hermeslogger.utils.DateFormat
import com.spartancookie.hermeslogger.utils.animateCopyToClipboardColor
import com.spartancookie.hermeslogger.utils.copyToClipboard
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat

private const val TAG = "InfoRecyclerAdapter"

internal class InfoRecyclerAdapter(private var eventList: MutableList<EventDataHolder>, activity: Activity, private val callback: LogSelectedCallback, private val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<InfoRecyclerAdapter.InfoViewHolder>() {

    private val actReference = WeakReference(activity)

    private val infoHolder get() = HermesHandler.infoHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_log_recycler, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val item = eventList[position]
        val format = SimpleDateFormat(DateFormat)

        val context = holder.entireView.context

        holder.title.text = item.message
        holder.date.text = format.format(item.creationDate.time)
        holder.id.text = item.id
        holder.icon.setImageDrawable(ContextCompat.getDrawable(context, item.type.drawableResource))
        holder.copyOrRemove.setOnClickListener {

            if (OverviewLayout.removeModeLiveData.value == true) {
                removeItemInPosition(position)
                infoHolder.removeLogById(item.id)
            } else {

                actReference.get()?.run {
                    copyToClipboard(this, item)
                    animateCopyToClipboardColor(it)
                } ?: Log.e(TAG, "There is no valid instance of an activity. data could not be copied successfully")
            }
        }
        holder.entireView.setOnClickListener { callback.onLogSelected(item) }

        // Observe changes on remove mode state change live data
        OverviewLayout.removeModeLiveData.observe(lifecycleOwner, { isEnabled ->
            holder.copyOrRemove.setImageDrawable(
                ContextCompat.getDrawable(context, getResource(isEnabled))
            )
        })

    }

    override fun getItemCount() = eventList.size

    class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val entireView = itemView
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val title: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.date)
        val copyOrRemove: ImageView = itemView.findViewById(R.id.copy_image_view)
        val id: TextView = itemView.findViewById(R.id.log_id_tv)
    }

    fun updateList(eventList: List<EventDataHolder>) {
        updateAllItems(eventList)
        notifyDataSetChanged()
    }

    fun updateListOnTop(eventList: List<EventDataHolder>, nrOfItemsAdded: Int) {
        updateAllItems(eventList)
        notifyItemRangeInserted(0, nrOfItemsAdded)
    }

    private fun removeItemInPosition(pos: Int) {
        this.eventList.removeAt(pos)
        notifyDataSetChanged()
    }

    //---------------- Helper methods ----------------

    private fun getResource(isRemoveModeEnabled: Boolean) = if (isRemoveModeEnabled) R.drawable.ic_hermes_logger_remove else R.drawable.ic_hermes_logger_copy

    private fun updateAllItems(eventList: List<EventDataHolder>) {
        this.eventList.clear()
        this.eventList.addAll(eventList)
    }

}