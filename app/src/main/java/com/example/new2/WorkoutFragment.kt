package com.example.new2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController

class WorkoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout, container, false)

        // Find the physicalActivity by its ID
        val physicalActivityImage = view.findViewById<ImageView>(R.id.physicalActivityImage)
        val physicalActivityName = view.findViewById<TextView>(R.id.physicalActivityName)
        val physicalActivityCard = view.findViewById<RelativeLayout>(R.id.physicalActivityCard)

        // Find the waterIntake by its ID
        val waterIntakeImg = view.findViewById<ImageView>(R.id.waterIntakeImage)
        val waterIntakeName = view.findViewById<TextView>(R.id.waterIntakeName)
        val waterIntakeCard = view.findViewById<RelativeLayout>(R.id.waterIntakeCard)

        // Set click listeners for the physicalActivity components
        physicalActivityImage.setOnClickListener {
            findNavController().navigate(R.id.action_workoutFragment_to_physicalActivityListFragment)
        }
        physicalActivityName.setOnClickListener {
            findNavController().navigate(R.id.action_workoutFragment_to_physicalActivityListFragment)
        }
        physicalActivityCard.setOnClickListener {
            findNavController().navigate(R.id.action_workoutFragment_to_physicalActivityListFragment)
        }

        // Set a click listener for the waterIntakeLayout
        waterIntakeImg.setOnClickListener {
            findNavController().navigate(R.id.action_workoutFragment_to_waterIntakeListFragment)
        }
        waterIntakeName.setOnClickListener {
            findNavController().navigate(R.id.action_workoutFragment_to_waterIntakeListFragment)
        }
        waterIntakeCard.setOnClickListener {
            findNavController().navigate(R.id.action_workoutFragment_to_waterIntakeListFragment)
        }

        return view
    }
}
