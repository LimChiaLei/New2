package com.example.new2

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ContentValues
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StepCountJobService : JobService() {
    companion object {
        private var stepCountResetCallback: StepCountResetCallback? = null

        // Setter for the callback
        fun setStepCountResetCallback(callback: StepCountResetCallback) {
            stepCountResetCallback = callback
        }
    }
    override fun onStartJob(params: JobParameters?): Boolean {
        // Get the current step count as a parameter (passed when scheduling the job)
        val stepCount = params?.extras?.getInt("stepCount") ?: 0

        // Get the current date
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)

        // Get the user ID from SharedPreferences
        val sharedPreferences = applicationContext.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)

        // Save the step count into the database
        val dbHelper = StepCountDatabaseHelper(applicationContext)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("step", stepCount)
            put("date", currentDate)
            put("userId", userId)
        }
        db.insert("stepCount", null, values)

        // Use the callback to reset the step count and the progress bar
        stepCountResetCallback?.resetStepCountAndProgressBar()

        // Job is complete
        jobFinished(params, false)
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}
