package com.example.new2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StepCountDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "stepCounter.db"
        const val TABLE_NAME = "stepCount"
        const val COLUMN_ID = "id"
        const val COLUMN_STEP = "step"
        const val COLUMN_DATE = "date"
        const val COLUMN_USER_ID = "userId"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME " +
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_STEP INTEGER, " +
                "$COLUMN_DATE TEXT, " +
                "$COLUMN_USER_ID TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Function to insert a new step count record into the database
    fun insertStepCount(step: Int, date: String, userId: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_STEP, step)
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_USER_ID, userId)

        // Insert the data into the table
        db.insert(TABLE_NAME, null, values)

        // Close the database connection
        db.close()
    }




}
