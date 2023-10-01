package com.example.new2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class DetailFragment : Fragment() {
    private lateinit var detailDesc: TextView
    private lateinit var detailTitle: TextView
    private lateinit var detailImage: ImageView

    // This callback will handle the back button press
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (childFragmentManager.backStackEntryCount > 0) {
                // If there are entries in the child fragment manager's back stack, pop them
                childFragmentManager.popBackStack()
            } else {
                // If the back stack is empty, navigate back to the previous destination
                findNavController().popBackStack()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        // Set up the back button callback
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        //--------------Back------------------
        val backBtn = view.findViewById<Button>(R.id.backBtn)

        backBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
            findNavController().popBackStack()
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
        //--------------Back------------------

        detailDesc = view.findViewById(R.id.detailDesc)
        detailTitle = view.findViewById(R.id.detailTitle)
        detailImage = view.findViewById(R.id.detailImage)

        val bundle = arguments
        if (bundle != null) {
            detailDesc.text = bundle.getString("Content") // Retrieve it as a String
            detailImage.setImageResource(bundle.getInt("Image"))
            detailTitle.text = bundle.getString("Title")
        }

        return view
    }
}



