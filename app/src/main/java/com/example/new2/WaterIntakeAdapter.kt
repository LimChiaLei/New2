package com.example.new2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.new2.R
import com.example.new2.WaterIntakeRecord

class WaterIntakeAdapter(private val waterIntakeRecords: MutableList<WaterIntakeRecord>) :
    RecyclerView.Adapter<WaterIntakeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.water_intake_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = waterIntakeRecords[position]

        // Bind your data to the UI elements in the item layout here
        holder.listDate.text = "Date: ${record.currentDate}"
        holder.listTime.text = "Time: ${record.currentTime}"

        // Set listDrinked text based on waterIntake value
        holder.listDrinked.text = "${record.waterIntake}ml"

        // Set listCup image based on waterIntake value
        val cupDrawableId = when (record.waterIntake) {
            60 -> R.drawable.cup60
            120 -> R.drawable.cup120
            180 -> R.drawable.cup180
            240 -> R.drawable.cup240
            else -> R.drawable.cup0
        }
        holder.listCup.setImageResource(cupDrawableId)
    }

    override fun getItemCount(): Int {
        return waterIntakeRecords.size
    }

    fun getItem(position: Int): WaterIntakeRecord {
        return waterIntakeRecords[position]
    }

    fun removeItem(position: Int) {
        waterIntakeRecords.removeAt(position)
        notifyItemRemoved(position)

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listDate: TextView = itemView.findViewById(R.id.listDate)
        val listTime: TextView = itemView.findViewById(R.id.listTime)
        val listDrinked: TextView = itemView.findViewById(R.id.listDrinked)
        val listCup: ImageView = itemView.findViewById(R.id.listCup)

    }
}
