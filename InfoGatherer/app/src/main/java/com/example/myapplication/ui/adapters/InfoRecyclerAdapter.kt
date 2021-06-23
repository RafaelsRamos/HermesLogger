package com.example.myapplication.ui.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.callbacks.SpecificItemCallback
import com.example.myapplication.models.LogDataHolder
import com.example.myapplication.utils.DateFormat
import com.example.myapplication.utils.animateCopyToClipboardColor
import com.example.myapplication.utils.copyToClipboard
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat

private const val TAG = "InfoRecyclerAdapter"

class InfoRecyclerAdapter(private var logList: List<LogDataHolder>, activity: Activity, private val callback: SpecificItemCallback): RecyclerView.Adapter<InfoRecyclerAdapter.InfoViewHolder>() {

    private val actReference = WeakReference(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_log_recycler, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val item = logList[position]
        val format = SimpleDateFormat(DateFormat)

        holder.title.text = item.message
        holder.date.text = format.format(item.creationDate.time)
        holder.id.text = item.id
        holder.icon.setImageDrawable(ContextCompat.getDrawable(holder.entireView.context, item.type.drawableResource))
        holder.copy.setOnClickListener {
            actReference.get()?.run {
                copyToClipboard(this, item)
                animateCopyToClipboardColor(it)
            } ?: Log.e(TAG, "There is no valid instance of an activity. data could not be copied successfully")
        }
        holder.entireView.setOnClickListener { callback.onSpecificItemClicked(item) }
    }

    override fun getItemCount() = logList.size

    class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val entireView = itemView
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val title: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.date)
        val copy: View = itemView.findViewById(R.id.copy_image_view)
        val id: TextView = itemView.findViewById(R.id.log_id_tv)
    }

    fun updateList(logList: List<LogDataHolder>) {
        this.logList = logList
        notifyDataSetChanged()
    }

    fun updateListOnTop(logList: List<LogDataHolder>, nrOfItemsAdded: Int) {
        this.logList = logList
        notifyItemRangeInserted(0, nrOfItemsAdded)
    }
}