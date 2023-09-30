package com.example.new2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context


class StepCountAdapter(private val context: Context, private val stepCountList: List<StepCountItem>) :
    RecyclerView.Adapter<StepCountAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.date)
        val stepsCountTextView: TextView = itemView.findViewById(R.id.stepsCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.step_record_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stepCount = stepCountList[position]
        holder.dateTextView.text = stepCount.date
        holder.stepsCountTextView.text = stepCount.stepCount.toString()
    }

    override fun getItemCount(): Int {
        return stepCountList.size
    }
}
