package com.example.new2

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StepCountRecordFragment : Fragment() {
    // Declare sharedPreferences and userId properties without initialization
    private lateinit var sharedPreferences: SharedPreferences
    private var userId: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Initialize sharedPreferences and userId when the fragment is attached
        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_step_count_record, container, false)

        // Initialize your RecyclerView and adapter
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val adapter = StepCountAdapter(requireContext(), getStepCountList(userId))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    // Function to retrieve step count data from the database
    private fun getStepCountList(userId: String?): List<StepCountItem> {
        val stepCountList = mutableListOf<StepCountItem>()
        val dbHelper = StepCountDatabaseHelper(requireContext())
        val db: SQLiteDatabase = dbHelper.readableDatabase

        // Define your query to retrieve data for the specific user
        val query = "SELECT ${StepCountDatabaseHelper.COLUMN_STEP}, ${StepCountDatabaseHelper.COLUMN_DATE} FROM ${StepCountDatabaseHelper.TABLE_NAME} WHERE ${StepCountDatabaseHelper.COLUMN_USER_ID} = ?"
        val selectionArgs = arrayOf(userId)

        val cursor: Cursor = db.rawQuery(query, selectionArgs)

        while (cursor.moveToNext()) {
            val step = cursor.getInt(0) // Assuming COLUMN_STEP is the first column (index 0)
            val date = cursor.getString(1) // Assuming COLUMN_DATE is the second column (index 1)
            val stepCount = StepCountItem(date, step)
            stepCountList.add(stepCount)
        }

        cursor.close()
        db.close()

        return stepCountList
    }
}
