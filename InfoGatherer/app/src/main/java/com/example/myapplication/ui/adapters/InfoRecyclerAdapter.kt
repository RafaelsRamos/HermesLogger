package com.example.myapplication.ui.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.callbacks.SpecificItemCallback
import com.example.myapplication.models.InfoDataHolder
import com.example.myapplication.utils.DateFormat
import com.example.myapplication.utils.copyToClipboard
import java.text.SimpleDateFormat

class InfoRecyclerAdapter(var infoList: List<InfoDataHolder>, private var activity: Activity, private val callback: SpecificItemCallback): RecyclerView.Adapter<InfoRecyclerAdapter.InfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val layoutInflater = LayoutInflater.from(activity.applicationContext)
        val view = layoutInflater.inflate(R.layout.item_log_recycler, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val item = infoList[position]
        val format = SimpleDateFormat(DateFormat)

        holder.title.text = item.msg
        holder.date.text = format.format(item.creationDate.time)
        holder.copy.setOnClickListener { copyToClipboard(activity, item) }
        holder.entireView.setOnClickListener { callback.onSpecificItemPressed(item) }
    }

    override fun getItemCount() = infoList.size

    class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val entireView = itemView
        val title: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.date)
        val copy: View = itemView.findViewById(R.id.copy_image_view)
    }

    fun updateList(infoList: List<InfoDataHolder>) {
        this.infoList = infoList
        notifyDataSetChanged()
    }
}