//package com.example.new2
//
//import androidx.room.Database
//
//
//
//@Database(entities = [MealPlanEntity::class], version = 1)
//abstract class MealPlanDatabase : RoomDatabase() {
//    abstract fun MealPlanDao(): MealPlanDao
//
////    companion object {
////        @Volatile
////        private var INSTANCE: MealPlanDatabase? = null
////
////        fun getDatabase(context: Context): MealPlanDatabase {
////            return INSTANCE ?: synchronized(this) {
////                val instance = Room.databaseBuilder(
////                    context.applicationContext,
////                    MealPlanDatabase::class.java,
////                    "app_database"
////                ).build()
////                INSTANCE = instance
////                instance
////            }
////        }
////    }
//}