package com.example.new2

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SwipeToDeleteWI(
    private val adapter: WaterIntakeAdapter,
    private val db: FirebaseFirestore,
    private val fragment: Fragment,
    private val waterIntakeRecords: MutableList<WaterIntakeRecord>
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition

        // Check if the position is within the valid range
        if (position != RecyclerView.NO_POSITION && waterIntakeRecords.isNotEmpty()) {
            val waterIntakeRecord = adapter.getItem(position)

            if (direction == ItemTouchHelper.RIGHT) {
                // Handle right swipe for editing
                val bundle = Bundle()
                bundle.putSerializable("waterIntakeRecord", waterIntakeRecord)

                // Navigate to the WaterIntakeEditFragment with data
                fragment.findNavController()
                    .navigate(R.id.action_waterIntakeListFragment_to_waterIntakeEditFragment, bundle)
            } else if (direction == ItemTouchHelper.LEFT) {
                // Delete the entire collection where userId, date, and time match
                db.collection("waterIntake")
                    .whereEqualTo("userId", waterIntakeRecord.userId)
                    .whereEqualTo("currentDate", waterIntakeRecord.currentDate)
                    .whereEqualTo("currentTime", waterIntakeRecord.currentTime)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            document.reference.delete()
                        }
                        // Remove the item from the RecyclerView
                        adapter.removeItem(position)
                    }
                    .addOnFailureListener { exception ->
                        // Handle the error
                        Log.e(TAG, "Error deleting documents", exception)
                    }
            }
        }
    }
}

