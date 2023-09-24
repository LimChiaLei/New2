package com.example.new2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(private val context: Context, private var dataList: List<DataClass>) : RecyclerView.Adapter<MyViewHolder>() {

    fun setSearchList(dataSearchList: List<DataClass>) {
        this.dataList = dataSearchList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]

        holder.recImage.setImageResource(data.dataImage)
        holder.recTitle.text = data.dataTitle
        holder.recDesc.text = data.dataDesc // Remove .toString()
        holder.recLang.text = data.dataLang

//        holder.recCard.setOnClickListener {
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("Image", data.dataImage)
//            intent.putExtra("Title", data.dataTitle)
//            intent.putExtra("Desc", data.dataDesc)
//
//            context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var recImage: ImageView = itemView.findViewById(R.id.recImage)
    var recTitle: TextView = itemView.findViewById(R.id.recTitle)
    var recDesc: TextView = itemView.findViewById(R.id.recDesc)
    var recLang: TextView = itemView.findViewById(R.id.recLang)
    var recCard: CardView = itemView.findViewById(R.id.recCard)
}
