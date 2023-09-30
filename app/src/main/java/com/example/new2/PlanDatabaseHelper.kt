//package com.example.new2
//
//import android.annotation.SuppressLint
//import android.app.DownloadManager.COLUMN_ID
//import android.content.ContentValues
//import android.content.Context
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//
//class PlanDatabaseHelper(private val context: Context):
//    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//    companion object{
//        private const val DATABASE_NAME = "plan.db"
//        private const val DATABASE_VERSION = 2
//        private const val TABLE_NAME = "plan"
//        private const val COLUMN_PLAN_ID = "planid"
//        private const val COLUMN_TITLE = "title"
//        private const val COLUMN_TYPE = "type"
//        private const val COLUMN_CONTENT = "content"
//        private const val COLUMN_IMG = "img"
//    }
//
//    override fun onCreate(db: SQLiteDatabase?) {
//        val createTableQuery = ("CREATE TABLE $TABLE_NAME(" +
//                "$COLUMN_PLAN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "$COLUMN_TITLE TEXT," +
//                "$COLUMN_TYPE TEXT," +
//                "$COLUMN_CONTENT TEXT," +
//                "$COLUMN_IMG TEXT)")
//        db?.execSQL(createTableQuery)
//    }
//
//    fun createPlanTable() {
//        val db = writableDatabase
//        val createTableQuery = ("CREATE TABLE IF NOT EXISTS $TABLE_NAME(" +
//                "$COLUMN_PLAN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "$COLUMN_TITLE TEXT," +
//                "$COLUMN_TYPE TEXT," +
//                "$COLUMN_CONTENT TEXT," +
//                "$COLUMN_IMG TEXT)")
//        db.execSQL(createTableQuery)
//    }
//
//
//    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
//        db?.execSQL(dropTableQuery)
//        onCreate(db)
//    }
//
//    fun insertPlan(plan: PlanDataClass): Long {
//        val values = ContentValues().apply {
//            put(COLUMN_TITLE, plan.title)
//            put(COLUMN_TYPE, plan.type)
//            put(COLUMN_CONTENT, plan.content)
//            put(COLUMN_IMG, plan.img)
//        }
//        val db = writableDatabase
//        return db.insert(TABLE_NAME, null, values)
//    }
//
//
//    @SuppressLint("Range")
//    fun readPlan(planid: String): PlanDataClass? {
//        val db = readableDatabase
//        val selection = "$COLUMN_PLAN_ID = ?"
//        val selectionArgs = arrayOf(planid)
//        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
//
//        var plan: PlanDataClass? = null
//
//        if (cursor.moveToFirst()) {
//            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_PLAN_ID))
//            val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
//            val type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE))
//            val content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
//            val img = cursor.getString(cursor.getColumnIndex(COLUMN_IMG))
//
//            // Create a Plan object with the retrieved data
//            plan = PlanDataClass(title, type, content, img)
//        }
//
//        cursor.close()
//        return plan
//    }
//
//    fun isPlanTableExists(): Boolean {
//        val db = readableDatabase
//        val query = "SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_NAME'"
//        val cursor = db.rawQuery(query, null)
//        val tableExists = cursor.moveToFirst()
//        cursor.close()
//        return tableExists
//    }
//
//    @SuppressLint("Range")
//    fun getPlanById(planId: Long): PlanDataClass? {
//        val db = this.readableDatabase
//        var plan: PlanDataClass? = null
//
//        val cursor = db.query(
//            TABLE_NAME,
//            null,
//            "$COLUMN_PLAN_ID = ?",
//            arrayOf(planId.toString()),
//            null,
//            null,
//            null
//        )
//
//        if (cursor.moveToFirst()) {
//            val id = cursor.getLong(cursor.getColumnIndex(COLUMN_PLAN_ID))
//            val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
//            val type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE))
//            val content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
//            val img = cursor.getString(cursor.getColumnIndex(COLUMN_IMG))
//
//            plan = PlanDataClass(id, title, type, content, img)
//        }
//
//        cursor.close()
//        db.close()
//
//        return plan
//    }
//
//    // Function to retrieve all plans from the database
//    fun getAllPlans(): List<PlanDataClass> {
//        val planList = mutableListOf<PlanDataClass>()
//        val db = this.readableDatabase
//
//        val query = "SELECT * FROM $TABLE_NAME"
//        val cursor = db.rawQuery(query, null)
//
//        try {
//            while (cursor.moveToNext()) {
//                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PLAN_ID))
//                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
//                val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
//                val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
//                val img = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMG))
//
//                // Create a PlanDataClass object and add it to the list
//                val plan = PlanDataClass(id, title, type, content, img)
//                planList.add(plan)
//            }
//        } finally {
//            cursor.close()
//            db.close()
//        }
//
//        return planList
//    }
//}
