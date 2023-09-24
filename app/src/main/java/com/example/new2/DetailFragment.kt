package com.example.new2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.new2.R

class DetailFragment : Fragment() {
    private lateinit var detailDesc: TextView
    private lateinit var detailTitle: TextView
    private lateinit var detailImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_detail, container, false)

        detailDesc = rootView.findViewById(R.id.detailDesc)
        detailTitle = rootView.findViewById(R.id.detailTitle)
        detailImage = rootView.findViewById(R.id.detailImage)

        val bundle = arguments
        if (bundle != null) {
            detailDesc.text = bundle.getString("Desc") // Retrieve it as a String
            detailImage.setImageResource(bundle.getInt("Image"))
            detailTitle.text = bundle.getString("Title")
        }

        return rootView
    }
}
