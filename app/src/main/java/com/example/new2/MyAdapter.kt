package com.example.new2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(private val context: Context, private var dataList: List<PlanDataClass>) : RecyclerView.Adapter<MyViewHolder>() {

    fun setSearchList(dataSearchList: List<PlanDataClass>) {
        this.dataList = dataSearchList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.recImage.setImageResource(data.img)
        holder.recTitle.text = data.title
        holder.recCont.text = data.content
        holder.recType.text = data.type

//        holder.recCard.setOnClickListener {
//            val intent = Intent(context, DetailFragment::class.java)
//            intent.putExtra("Image", data.img)
//            intent.putExtra("Title", data.title)
//            intent.putExtra("Content", data.content)
//
//            context.startActivity(intent)
//        }
        holder.recCard.setOnClickListener {
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            val detailFragment = DetailFragment() // Create an instance of your DetailFragment


            val args = Bundle()
            args.putInt("Image", data.img)
            args.putString("Title", data.title)
            args.putString("Content", data.content)
            detailFragment.arguments = args

            // Replace the current fragment with the DetailFragment
            fragmentTransaction.replace(R.id.detailFragment, detailFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var recImage: ImageView = itemView.findViewById(R.id.recImage)
    var recTitle: TextView = itemView.findViewById(R.id.recTitle)
    var recCont: TextView = itemView.findViewById(R.id.recCont)
    var recType: TextView = itemView.findViewById(R.id.recType)
    var recCard: CardView = itemView.findViewById(R.id.recCard)
}


