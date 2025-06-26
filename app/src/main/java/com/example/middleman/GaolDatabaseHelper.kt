package com.example.middleman

import android.content.ContentValues
import android.content.Context
import android.content.CursorLoader
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GaolDatabaseHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery = "CREATE TABLE $TABLE_NAME(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_CATEGORY TEXT," +
                "$COLUMN_TOTAL DOUBLE," +
                "$COLUMN_CURRENT_AMOUNT DOUBLE)"
        db?.execSQL(createQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        val dropQuery ="DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropQuery)
        onCreate(db)
    }

    fun createNewGoal(goal: Goal){
        val database = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_CATEGORY, goal.category)
            put(COLUMN_TOTAL,goal.totalAmount)
            put(COLUMN_CURRENT_AMOUNT,goal.currentAmount)
        }
        database.insert(TABLE_NAME,null,contentValues)
    }

    fun getGoals(): List<Goal>{
        val database = readableDatabase
        val goalsList = mutableListOf<Goal>()
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = database.rawQuery(query,null)

        while(cursor.moveToNext()){
            val goalId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
            val totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL))
            val currentAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_AMOUNT))

            if(totalAmount > currentAmount){
                val goal = Goal(goalId,category,totalAmount,currentAmount)
                goalsList.add(goal)
            }

        }
        cursor.close()
        database.close()
        return goalsList
    }
    fun getCompletedGoals(): List<Goal>{
        val database = readableDatabase
        val goalsList = mutableListOf<Goal>()
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = database.rawQuery(query,null)

        while(cursor.moveToNext()){
            val goalId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
            val totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL))
            val currentAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_AMOUNT))

            if(totalAmount <= currentAmount){
                val goal = Goal(goalId,category,totalAmount,currentAmount)
                goalsList.add(goal)
            }

        }
        cursor.close()
        database.close()
        return goalsList
    }

    fun totalGoals():Double{
        val database = readableDatabase
        var goalTotal = 0.0
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = database.rawQuery(query,null)

        while(cursor.moveToNext()){
            val currentAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_AMOUNT))
            val totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL))

            if(currentAmount < totalAmount){
                goalTotal = goalTotal + totalAmount
            }

        }
        cursor.close()
        database.close()
        return goalTotal
    }
    fun totalCompletedGoals():Double{
        val database = readableDatabase
        var goalTotal = 0.0
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = database.rawQuery(query,null)

        while(cursor.moveToNext()){
            val currentAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_AMOUNT))
            val totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL))
            if(currentAmount >= totalAmount){
                goalTotal = goalTotal + totalAmount
            }

        }
        cursor.close()
        database.close()
        return goalTotal
    }

    fun getGoalById(goalId:Int): Goal{
        val database = readableDatabase
        val goalsList = mutableListOf<Goal>()
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $goalId"
        val cursor = database.rawQuery(query,null)

        while(cursor.moveToNext()){
            val goalId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
            val totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL))
            val currentAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_AMOUNT))

            val goal = Goal(goalId,category,totalAmount,currentAmount)
            goalsList.add(goal)
        }
        cursor.close()
        database.close()
        return goalsList[0]
    }

    fun updateGoal(goal: Goal,goalId: Int){
        val database = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_CATEGORY, goal.category)
            put(COLUMN_TOTAL,goal.totalAmount)
            put(COLUMN_CURRENT_AMOUNT,goal.currentAmount)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(goalId.toString())
        database.update(TABLE_NAME,contentValues,whereClause,whereArgs)
        database.close()
    }

    fun deleteGoal(goalId: Int){
        val database = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(goalId.toString())
        database.delete(TABLE_NAME,whereClause,whereArgs)
        database.close()
    }


    companion object{
        private const val DATABASE_NAME = "GoalsDB"
        private const val DATABASE_VERSION = 9
        private const val TABLE_NAME = "GoalsTable"
        private const val COLUMN_ID = "goalId"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_TOTAL ="total"
        private const val COLUMN_CURRENT_AMOUNT = "current"

    }
}