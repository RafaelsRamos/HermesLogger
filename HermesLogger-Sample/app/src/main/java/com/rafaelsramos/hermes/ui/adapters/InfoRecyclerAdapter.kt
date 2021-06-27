package com.rafaelsramos.hermes.ui.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.rafaelsramos.hermes.R
import com.rafaelsramos.hermes.callbacks.SpecificItemCallback
import com.rafaelsramos.hermes.debugToaster.Toaster
import com.rafaelsramos.hermes.models.LogDataHolder
import com.rafaelsramos.hermes.ui.components.OverviewLayout
import com.rafaelsramos.hermes.utils.DateFormat
import com.rafaelsramos.hermes.utils.animateCopyToClipboardColor
import com.rafaelsramos.hermes.utils.copyToClipboard
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat

private const val TAG = "InfoRecyclerAdapter"

internal class InfoRecyclerAdapter(private var logList: MutableList<LogDataHolder>, activity: Activity, private val callback: SpecificItemCallback, private val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<InfoRecyclerAdapter.InfoViewHolder>() {

    private val actReference = WeakReference(activity)

    private val infoHolder get() = Toaster.instance.infoHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_log_recycler, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val item = logList[position]
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
        holder.entireView.setOnClickListener { callback.onSpecificItemClicked(item) }

        // Observe changes on remove mode state change live data
        OverviewLayout.removeModeLiveData.observe(lifecycleOwner, Observer { isEnabled ->
            holder.copyOrRemove.setImageDrawable(
                ContextCompat.getDrawable(context, getResource(isEnabled))
            )
        })

    }

    override fun getItemCount() = logList.size

    class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val entireView = itemView
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val title: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.date)
        val copyOrRemove: ImageView = itemView.findViewById(R.id.copy_image_view)
        val id: TextView = itemView.findViewById(R.id.log_id_tv)
    }

    fun updateList(logList: List<LogDataHolder>) {
        this.logList = logList.toMutableList()
        notifyDataSetChanged()
    }

    fun updateListOnTop(logList: List<LogDataHolder>, nrOfItemsAdded: Int) {
        this.logList = logList.toMutableList()
        notifyItemRangeInserted(0, nrOfItemsAdded)
    }

    private fun removeItemInPosition(pos: Int) {
        this.logList.removeAt(pos)
        notifyDataSetChanged()
    }

    //---------------- Helper methods ----------------

    private fun getResource(isRemoveModeEnabled: Boolean) = if (isRemoveModeEnabled) R.drawable.ic_remove else R.drawable.ic_copy

}