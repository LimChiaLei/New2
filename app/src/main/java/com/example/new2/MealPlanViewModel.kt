//package com.example.new2
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class mealPlanViewModel(private val repository: mealPlanRepository) : ViewModel() {
//
//    fun insertMealPlan(title: String, img: String, content: String) {
//        val mealPlanEntity = MealPlanEntity(title = title, img = img, content = content)
//
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.insert(mealPlanEntity)
//        }
//    }
//}
