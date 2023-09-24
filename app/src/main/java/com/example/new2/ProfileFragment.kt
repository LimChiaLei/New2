package com.example.new2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController

class ProfileFragment : Fragment() {
    // Retrieve the user's ID from SharedPreferences
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Retrieve the user's ID from SharedPreferences
        val userId = sharedPreferences.getString("userId", null)

        // Find the TextView by its ID
        val testText = view.findViewById<TextView>(R.id.userName)

        // Set the user ID as the text of the TextView
        testText.text = userId ?: "User ID not found" // Show a default message if the ID is null

        // Find the ImgView by its ID
        val profilePicImageView = view.findViewById<ImageView>(R.id.profilePic)

        // Set an OnClickListener to navigate to ViewProfileFragment
        profilePicImageView.setOnClickListener {
            // Navigate to the ViewProfileFragment
            findNavController().navigate(R.id.action_profileFragment_to_viewProfileFragment)
        }
        return view
    }


}