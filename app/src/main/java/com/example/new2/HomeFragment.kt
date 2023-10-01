package com.example.new2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: MutableList<PlanDataClass>
    private lateinit var adapter: MyAdapter
    private lateinit var androidData: PlanDataClass
    private lateinit var searchView: SearchView

    //bottom
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        searchView = view.findViewById(R.id.search)
        //-----------Search------------------
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                searchList(newText)
                return true
            }
        })
        //-----------Search------------------
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = mutableListOf()

        androidData = PlanDataClass("Beginner", getString(R.string.beginner_plan), getString(R.string.meal_plan), R.drawable.meal_plan)
        dataList.add(androidData)

        androidData = PlanDataClass("Keep Fit", getString(R.string.keep_fit_plan), getString(R.string.meal_plan), R.drawable.keepfit_plan)
        dataList.add(androidData)

        androidData = PlanDataClass("7-Day Weight Loss Meal Plan", getString(R.string.weight_loss_plan), getString(R.string.meal_plan), R.drawable.weightloss_plan)
        dataList.add(androidData)

        androidData = PlanDataClass("High-Protein Meal Plan for Muscle Building", getString(R.string.muscle_building_plan), getString(R.string.meal_plan), R.drawable.highprotein_plan)
        dataList.add(androidData)

        adapter = MyAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter

        //-------------------------BottomNavigationView---------------------------------
        val bottomNavView = view?.findViewById<BottomNavigationView>(R.id.bottomNavView)
        if (bottomNavView != null) {
            bottomNavView.selectedItemId = R.id.home
        }
        //-------------------------BottomNavigationView---------------------------------
        return view
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
                    true
                }
                R.id.workout -> {
                    // Navigate to WorkoutFragment
                    navController.navigate(R.id.workoutFragment)
                    true
                }
                R.id.chart -> {
                    // Navigate to ChartFragment
                    navController.navigate(R.id.chartFragment)
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

    private fun searchList(text: String?) {
        val dataSearchList = mutableListOf<PlanDataClass>()
        for (data in dataList) {
            if (data.title.toLowerCase(Locale.ROOT).contains(text?.toLowerCase(Locale.ROOT) ?: "")) {
                dataSearchList.add(data)
            }
        }
        if (dataSearchList.isEmpty()) {
            Toast.makeText(requireContext(), "Not Found", Toast.LENGTH_SHORT).show()
        } else {
            adapter.setSearchList(dataSearchList)
        }
    }

}

