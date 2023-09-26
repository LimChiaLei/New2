package com.example.new2


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SwipeToDeletePA(
    private val adapter: PhysicalActivityAdapter,
    private val db: FirebaseFirestore,
    private val fragment: Fragment // Pass the current fragment for navigation


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
        val physicalActivity = adapter.getItem(position)

        if (direction == ItemTouchHelper.RIGHT) {
            // Handle right swipe for editing
            val bundle = Bundle()
            bundle.putSerializable("physicalActivity", physicalActivity)

            // Navigate to the EditPhysicalActivityFragment with data
            fragment.findNavController().navigate(R.id.action_physicalActivityListFragment_to_editPhysicalActivityFragment, bundle)
        } else if (direction == ItemTouchHelper.LEFT) {
            // Handle left swipe for deletion
            db.collection("physicalActivity")
                .document(physicalActivity.id)
                .delete()
                .addOnSuccessListener {
                    // Remove the item from the RecyclerView
                    adapter.removeItem(position)
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Log.e(TAG, "Error deleting document", exception)
                }
        }
    }
}
