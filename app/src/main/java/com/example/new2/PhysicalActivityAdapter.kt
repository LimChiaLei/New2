package com.example.new2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.new2.PhysicalActivity
import com.example.new2.R

class PhysicalActivityAdapter(private val data: MutableList<PhysicalActivity>) :
    RecyclerView.Adapter<PhysicalActivityAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.listDate)
        val timeTextView: TextView = itemView.findViewById(R.id.listTime)
        val titleTextView: TextView = itemView.findViewById(R.id.listTitle)
        val hrsTextView: TextView = itemView.findViewById(R.id.listHrs)
        val minsTextView: TextView = itemView.findViewById(R.id.listMins)
        val userIdTextView: TextView = itemView.findViewById(R.id.userId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.physical_activity_item, parent, false) // Replace with your layout
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        // Bind data to views
        holder.dateTextView.text = item.date
        holder.timeTextView.text = item.time
        holder.titleTextView.text = item.title
        holder.hrsTextView.text = "${item.hours} hrs" // Format duration
        holder.minsTextView.text = "${item.minute} mins" // Format duration
        holder.userIdTextView.text = item.userId
        // You can customize the format further if needed
    }

    fun getItem(position: Int): PhysicalActivity {
        return data[position]
    }

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)

    }

    override fun getItemCount(): Int {
        return data.size
    }
}