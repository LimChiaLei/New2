package com.example.new2

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart

import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Define a callback interface
interface StepCountResetCallback {
    fun resetStepCountAndProgressBar()
}

private const val JOB_ID = 123 // You can choose any unique integer value

class ChartFragment : Fragment(), SensorEventListener {

    // Retrieve the user's ID from SharedPreferences
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    private lateinit var navController: NavController

    private var mSensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var totalSteps = 0
    private var previousTotalSteps = 0
    private var progressBar: ProgressBar? = null
    private var steps: TextView? = null

    lateinit var radarChart: RadarChart
    lateinit var barChart: BarChart
    lateinit var pieChart: PieChart
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chart, container, false)

        // Retrieve the user's ID from SharedPreferences
        val userId = sharedPreferences.getString("userId", null)

        // Set up the BottomNavigationView
        val bottomNavView = view?.findViewById<BottomNavigationView>(R.id.bottomNavView)
        if (bottomNavView != null) {
            bottomNavView.selectedItemId = R.id.chart
        }

        //------------Steps Count----------------
        progressBar = view.findViewById(R.id.progressBar)
        steps = view.findViewById(R.id.steps)

        mSensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        resetSteps() // Call the resetSteps function here

        // Update the TextView with the current step count
        val stepsTextView: TextView = view.findViewById(R.id.steps)
        stepsTextView.text = getCurrentStepCount().toString()

        if (stepsTextView != null) {
            // The view with ID "steps" was found, so you can safely access it.
            stepsTextView.text = getCurrentStepCount().toString()
        } else {
            // Handle the case where the view with ID "steps" was not found.
            // For example, you can log an error or show a message.
            Log.e("ChartFragment", "stepsTextView not found")
        }

        // To save the step count and reset daily, you can do this in your code
        val stepCount = stepsTextView.text.toString().toInt()
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        insertStepCount(stepCount, currentDate, userId)
        resetStepCountDaily()


        //------------Steps Count----------------

        //------------Radar-------------------
        var runningHrs: String? = null
        if (userId != null) {
            totalHrsRunning(userId) { totalHours ->
                // Use the total hours here
                runningHrs = totalHours.toString()
            }
        }
        var walkingHrs: String? = null
        if (userId != null) {
            totalHrsWalking(userId) { totalHours ->
                // Use the total hours here
                walkingHrs = totalHours.toString()
            }
        }
        var cyclingHrs: String? = null
        if (userId != null) {
            totalHrsCycling(userId) { totalHours ->
                // Use the total hours here
                cyclingHrs = totalHours.toString()
            }
        }
        var swimmingHrs: String? = null
        if (userId != null) {
            totalHrsSwimming(userId) { totalHours ->
                // Use the total hours here
                swimmingHrs = totalHours.toString()
            }
        }
        var sportHrs: String? = null
        if (userId != null) {
            totalHrsSport(userId) { totalHours ->
                // Use the total hours here
                sportHrs = totalHours.toString()
            }
        }
        var yogaHrs: String? = null
        if (userId != null) {
            totalHrsYoga(userId) { totalHours ->
                // Use the total hours here
                yogaHrs = totalHours.toString()
            }
        }
        var gymHrs: String? = null
        if (userId != null) {
            totalHrsGym(userId) { totalHours ->
                // Use the total hours here
                gymHrs = totalHours.toString()
            }
        }



        radarChart = view.findViewById(R.id.radar_chart)
        val labels = listOf("Running", "Walking", "Cycling", "Swimming", "Sport", "Yoga", "Gym")
        val xAxis = radarChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.axisLineWidth =2f
        xAxis.textSize=10f


        val list:ArrayList<RadarEntry> = ArrayList()
        list.add(RadarEntry(30F))
        list.add(RadarEntry(50F))
        list.add(RadarEntry(20F))
        list.add(RadarEntry(40F))
        list.add(RadarEntry(50F))
        list.add(RadarEntry(55F))
        list.add(RadarEntry(40F))
        if (runningHrs != null) {
            list.add(RadarEntry(runningHrs!!.toFloat()))
        }
        if (walkingHrs != null) {
            list.add(RadarEntry(walkingHrs!!.toFloat()))
        }
        if (cyclingHrs != null) {
            list.add(RadarEntry(cyclingHrs!!.toFloat()))
        }
        if (swimmingHrs != null) {
            list.add(RadarEntry(swimmingHrs!!.toFloat()))
        }
        if (sportHrs != null) {
            list.add(RadarEntry(sportHrs!!.toFloat()))
        }
        if (yogaHrs != null) {
            list.add(RadarEntry(yogaHrs!!.toFloat()))
        }
        if (gymHrs != null) {
            list.add(RadarEntry(gymHrs!!.toFloat()))
        }

        val radarDataSet=RadarDataSet(list,"List")

        radarDataSet.lineWidth=2f
        radarDataSet.valueTextColor = Color.BLUE
        radarDataSet.valueTextSize=14f

        val radarData=RadarData()
        radarData.addDataSet(radarDataSet)

        radarChart.data=radarData
        radarChart.description.text= ""
        radarChart.animateY(2000)
        //------------Radar-------------------

//        //------------Bar-------------------
//        barChart = view.findViewById(R.id.bar_chart)
//
//        val list2: ArrayList<BarEntry> = ArrayList()
//        list2.add(BarEntry(100f, 100f))
//        list2.add(BarEntry(101f, 200f))
//        list2.add(BarEntry(102f, 300f))
//        list2.add(BarEntry(103f, 400f))
//        list2.add(BarEntry(104f, 500f))
//
//        val barDataSet = BarDataSet(list2, "List")
//
//        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
//        barDataSet.valueTextColor = Color.BLACK
//
//        val barData = BarData(barDataSet)
//
//        barChart.setFitBars(true)
//
//        barChart.data = barData
//
//        barChart.description.text = "Bar Chart"
//
//        barChart.animateY(2000)
//        //------------Bar-------------------
//
//        //------------Pie-------------------
//        pieChart = view.findViewById(R.id.pie_chart)
//
//        val list3: ArrayList<PieEntry> = ArrayList()
//        list3.add(PieEntry(100f, "100"))
//        list3.add(PieEntry(101f, "101"))
//        list3.add(PieEntry(102f, "102"))
//        list3.add(PieEntry(103f, "103"))
//        list3.add(PieEntry(104f, "104"))
//
//        val pieDataSet = PieDataSet(list3, "List")
//
//        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
//        pieDataSet.valueTextColor = Color.BLACK
//        pieDataSet.valueTextSize = 15f
//
//        val pieData = PieData(pieDataSet)
//
//        pieChart.data = pieData
//
//        pieChart.description.text = "Pie Chart"
//
//        pieChart.centerText = "List"
//
//        pieChart.animateY(2000)

        //------------Pie-------------------


        return view
    }

    override fun onResume() {
        super.onResume()

        if (stepSensor == null) {
            Toast.makeText(requireContext(), "This device has no sensor", Toast.LENGTH_SHORT).show()
        } else {
            mSensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()

        mSensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = event.values[0].toInt()
            val currentSteps = totalSteps - previousTotalSteps
            steps?.text = currentSteps.toString()
            progressBar?.progress = currentSteps
        }
    }

    private fun resetSteps() {
        steps?.setOnClickListener {
            Toast.makeText(requireContext(), "Long press to reset steps", Toast.LENGTH_SHORT).show()
        }
        steps?.setOnLongClickListener {
            previousTotalSteps = totalSteps
            steps?.text = "0"
            progressBar?.progress = 0
            saveData()
            true
        }
    }

    private fun saveData() {
        val sharedPref = requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("key1", previousTotalSteps.toString())
        editor.apply()
    }

    private fun loadData() {
        val sharedPref = requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val savedNumber = sharedPref.getString("key1", "0")?.toInt() ?: 0
        previousTotalSteps = savedNumber
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Empty implementation, as this method is required by SensorEventListener
    }


    private fun insertStepCount(step: Int, date: String, userId: String?) {
        val dbHelper = StepCountDatabaseHelper(requireContext())
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        values.put(StepCountDatabaseHelper.COLUMN_STEP, step)
        values.put(StepCountDatabaseHelper.COLUMN_DATE, date)
        values.put(StepCountDatabaseHelper.COLUMN_USER_ID, userId)

        db.insert(StepCountDatabaseHelper.TABLE_NAME, null, values)
        db.close()
    }
    private fun resetStepCountDaily() {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val dbHelper = StepCountDatabaseHelper(requireContext())
        val db = dbHelper.writableDatabase

        db.delete(StepCountDatabaseHelper.TABLE_NAME, "${StepCountDatabaseHelper.COLUMN_DATE} < ?", arrayOf(currentDate))
        db.close()
    }
    // Function to get the current step count from the TextView
    private fun getCurrentStepCount(): Int {
        val stepsTextView: TextView? = view?.findViewById(R.id.steps)
        val stepCountText = stepsTextView?.text?.toString() ?: "0"
        return try {
            stepCountText.toInt()
        } catch (e: NumberFormatException) {
            0 // Handle the case where the text is not a valid integer
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the NavController
        navController = requireActivity().findNavController(R.id.nav_host_fragment)

        // Set up the BottomNavigationView
        val bottomNavView = view.findViewById<BottomNavigationView>(R.id.bottomNavView)
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // Navigate to HomeFragment
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.workout -> {
                    // Navigate to WorkoutFragment
                    navController.navigate(R.id.workoutFragment)
                    true
                }
                R.id.chart -> {
                    true
                }
                R.id.profile -> {
                    // Navigate to ProfileFragment
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }
    }
    fun totalHrsRunning(userId: String, callback: (Double) -> Unit) {
        // Create a reference to the "physicalActivity" collection
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("physicalActivity")

        collectionRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("type", "Running")
            .get()
            .addOnSuccessListener { documents ->
                var totalHours = 0.0
                var totalMinutes = 0.0

                for (document in documents) {
                    val hours = document.getString("hours")?.toDouble() ?: 0.0
                    val minutes = document.getString("minutes")?.toDouble() ?: 0.0

                    totalHours += hours
                    totalMinutes += minutes
                }

                // Calculate the total hours including minutes
                val totalHrs = totalHours + (totalMinutes / 60.0)

                // Invoke the callback with the result
                callback(totalHrs)
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error getting documents: ", exception)
                // You can also invoke the callback with an error value if needed
            }
    }
    fun totalHrsCycling(userId: String, callback: (Double) -> Unit) {
        // Create a reference to the "physicalActivity" collection
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("physicalActivity")

        collectionRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("type", "Cycling")
            .get()
            .addOnSuccessListener { documents ->
                var totalHours = 0.0
                var totalMinutes = 0.0

                for (document in documents) {
                    val hours = document.getString("hours")?.toDouble() ?: 0.0
                    val minutes = document.getString("minutes")?.toDouble() ?: 0.0

                    totalHours += hours
                    totalMinutes += minutes
                }

                // Calculate the total hours including minutes
                val totalHrs = totalHours + (totalMinutes / 60.0)

                // Invoke the callback with the result
                callback(totalHrs)
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error getting documents: ", exception)
                // You can also invoke the callback with an error value if needed
            }
    }
    fun totalHrsWalking(userId: String, callback: (Double) -> Unit) {
        // Create a reference to the "physicalActivity" collection
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("physicalActivity")

        collectionRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("type", "Walking")
            .get()
            .addOnSuccessListener { documents ->
                var totalHours = 0.0
                var totalMinutes = 0.0

                for (document in documents) {
                    val hours = document.getString("hours")?.toDouble() ?: 0.0
                    val minutes = document.getString("minutes")?.toDouble() ?: 0.0

                    totalHours += hours
                    totalMinutes += minutes
                }

                // Calculate the total hours including minutes
                val totalHrs = totalHours + (totalMinutes / 60.0)

                // Invoke the callback with the result
                callback(totalHrs)
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error getting documents: ", exception)
                // You can also invoke the callback with an error value if needed
            }
    }
    fun totalHrsSwimming(userId: String, callback: (Double) -> Unit) {
        // Create a reference to the "physicalActivity" collection
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("physicalActivity")

        collectionRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("type", "Swimming")
            .get()
            .addOnSuccessListener { documents ->
                var totalHours = 0.0
                var totalMinutes = 0.0

                for (document in documents) {
                    val hours = document.getString("hours")?.toDouble() ?: 0.0
                    val minutes = document.getString("minutes")?.toDouble() ?: 0.0

                    totalHours += hours
                    totalMinutes += minutes
                }

                // Calculate the total hours including minutes
                val totalHrs = totalHours + (totalMinutes / 60.0)

                // Invoke the callback with the result
                callback(totalHrs)
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error getting documents: ", exception)
                // You can also invoke the callback with an error value if needed
            }
    }
    fun totalHrsSport(userId: String, callback: (Double) -> Unit) {
        // Create a reference to the "physicalActivity" collection
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("physicalActivity")

        collectionRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("type", "Sport")
            .get()
            .addOnSuccessListener { documents ->
                var totalHours = 0.0
                var totalMinutes = 0.0

                for (document in documents) {
                    val hours = document.getString("hours")?.toDouble() ?: 0.0
                    val minutes = document.getString("minutes")?.toDouble() ?: 0.0

                    totalHours += hours
                    totalMinutes += minutes
                }

                // Calculate the total hours including minutes
                val totalHrs = totalHours + (totalMinutes / 60.0)

                // Invoke the callback with the result
                callback(totalHrs)
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error getting documents: ", exception)
                // You can also invoke the callback with an error value if needed
            }
    }
    fun totalHrsYoga(userId: String, callback: (Double) -> Unit) {
        // Create a reference to the "physicalActivity" collection
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("physicalActivity")

        collectionRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("type", "Yoga")
            .get()
            .addOnSuccessListener { documents ->
                var totalHours = 0.0
                var totalMinutes = 0.0

                for (document in documents) {
                    val hours = document.getString("hours")?.toDouble() ?: 0.0
                    val minutes = document.getString("minutes")?.toDouble() ?: 0.0

                    totalHours += hours
                    totalMinutes += minutes
                }

                // Calculate the total hours including minutes
                val totalHrs = totalHours + (totalMinutes / 60.0)

                // Invoke the callback with the result
                callback(totalHrs)
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error getting documents: ", exception)
                // You can also invoke the callback with an error value if needed
            }
    }
    fun totalHrsGym(userId: String, callback: (Double) -> Unit) {
        // Create a reference to the "physicalActivity" collection
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("physicalActivity")

        collectionRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("type", "Gym")
            .get()
            .addOnSuccessListener { documents ->
                var totalHours = 0.0
                var totalMinutes = 0.0

                for (document in documents) {
                    val hours = document.getString("hours")?.toDouble() ?: 0.0
                    val minutes = document.getString("minutes")?.toDouble() ?: 0.0

                    totalHours += hours
                    totalMinutes += minutes
                }

                // Calculate the total hours including minutes
                val totalHrs = totalHours + (totalMinutes / 60.0)

                // Invoke the callback with the result
                callback(totalHrs)
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error getting documents: ", exception)
                // You can also invoke the callback with an error value if needed
            }
    }

}
