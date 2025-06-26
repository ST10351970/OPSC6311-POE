package com.example.middleman

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.annotation.ContentView
import java.util.zip.CheckedOutputStream
import kotlin.math.exp

class ExpenseDatabaseHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStatement = "CREATE TABLE $TABLE_NAME(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_CATEGORY TEXT," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_DATE DATE," +
                "$COLUMN_TIME TEXT," +
                "$COLUMN_AMOUNT DOUBLE, " +
                "$COLUMN_PICTURE BLOB)"
        db?.execSQL(createTableStatement)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        val destroyDatabase = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(destroyDatabase)
        onCreate(db)
    }

    fun createExpenseEntry(expense: Expense){
        val database = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE,expense.title)
            put(COLUMN_CATEGORY, expense.category)
            put(COLUMN_DESCRIPTION,expense.description)
            put(COLUMN_DATE,expense.date)
            put(COLUMN_TIME,expense.time)
            put(COLUMN_AMOUNT,expense.amount)
            put(COLUMN_PICTURE,expense.picture)
        }
        database.insert(TABLE_NAME,null,contentValues)
    }

    fun returnExpenses():List<Expense>{
        val database = readableDatabase
        val expenseRecords = mutableListOf<Expense>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val records = database.rawQuery(selectQuery,null)

        while(records.moveToNext()){
            val expenseId = records.getInt(records.getColumnIndexOrThrow(COLUMN_ID))
            val title = records.getString(records.getColumnIndexOrThrow(COLUMN_TITLE))
            val description = records.getString(records.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val date = records.getString(records.getColumnIndexOrThrow(COLUMN_DATE))
            val time = records.getString(records.getColumnIndexOrThrow(COLUMN_TIME))
            val category = records.getString(records.getColumnIndexOrThrow(COLUMN_CATEGORY))
            val amount = records.getDouble(records.getColumnIndexOrThrow(COLUMN_AMOUNT))
            val image = records.getBlob(records.getColumnIndexOrThrow(COLUMN_PICTURE))

            val transaction = Expense(expenseId,title,description,date,time,amount, category,image)
            expenseRecords.add(transaction)
        }
        records.close()
        database.close()
        return expenseRecords
    }
    fun getExpenseById(expenseId:Int):Expense{
        val database = readableDatabase
        val expenseRecords = mutableListOf<Expense>()
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $expenseId"
        val records = database.rawQuery(selectQuery,null)

        while(records.moveToNext()){
            val expenseId = records.getInt(records.getColumnIndexOrThrow(COLUMN_ID))
            val title = records.getString(records.getColumnIndexOrThrow(COLUMN_TITLE))
            val description = records.getString(records.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val date = records.getString(records.getColumnIndexOrThrow(COLUMN_DATE))
            val time = records.getString(records.getColumnIndexOrThrow(COLUMN_TIME))
            val category = records.getString(records.getColumnIndexOrThrow(COLUMN_CATEGORY))
            val amount = records.getDouble(records.getColumnIndexOrThrow(COLUMN_AMOUNT))
            val image = records.getBlob(records.getColumnIndexOrThrow(COLUMN_PICTURE))

            val transaction = Expense(expenseId,title,description,date,time,amount, category,image)
            expenseRecords.add(transaction)
        }
        records.close()
        database.close()
        return expenseRecords[0]
    }
    fun expenseTotal():Double{
        val database = readableDatabase
        var totalAccumulator = 0.0
        val select = "SELECT $COLUMN_AMOUNT FROM $TABLE_NAME"
        val cursor = database.rawQuery(select,null)

        while (cursor.moveToNext()){
            val amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT))
            totalAccumulator+=amount
        }
        cursor.close()
        database.close()
        return totalAccumulator

    }

    fun getCategoryTotals(): List<CategoryTotal>{
        val database = readableDatabase
        val categoryTotals = mutableListOf<CategoryTotal>()
        val selectQuery = "SELECT $COLUMN_CATEGORY ,SUM($COLUMN_AMOUNT) AS $COLUMN_AMOUNT FROM $TABLE_NAME " +
                "GROUP BY $COLUMN_CATEGORY"
        val records = database.rawQuery(selectQuery,null)

        while(records.moveToNext()){

            val category = records.getString(records.getColumnIndexOrThrow(COLUMN_CATEGORY))
            val amount = records.getDouble(records.getColumnIndexOrThrow(COLUMN_AMOUNT))
            val total = CategoryTotal(amount.toFloat(),category)

            categoryTotals.add(total)
        }
        records.close()
        database.close()
        return categoryTotals
    }

    fun updateExpenseEntry(expense: Expense,expenseId:Int){
        val database = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE,expense.title)
            put(COLUMN_CATEGORY, expense.category)
            put(COLUMN_DESCRIPTION,expense.description)
            put(COLUMN_DATE,expense.date)
            put(COLUMN_TIME,expense.time)
            put(COLUMN_AMOUNT,expense.amount)
            put(COLUMN_PICTURE,expense.picture)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(expenseId.toString())
        database.update(TABLE_NAME,contentValues,whereClause,whereArgs)
        database.close()
    }

    fun deleteExpense(expenseId:Int){
        val database = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(expenseId.toString())
        database.delete(TABLE_NAME,whereClause,whereArgs)
        database.close()
    }

    companion object{
        private const val DATABASE_NAME = "Expenses"
        private const val DATABASE_VERSION = 3
        private const val TABLE_NAME = "ExpensesTable"
        private const val COLUMN_ID = "ExpenseId"
        private const val COLUMN_TITLE = "Title"
        private const val COLUMN_DESCRIPTION = "Description"
        private const val COLUMN_DATE = "Date"
        private const val COLUMN_CATEGORY = "Category"
        private const val COLUMN_TIME = "Time"
        private const val COLUMN_AMOUNT = "Amount"
        private const val COLUMN_PICTURE = "Picture"
    }
}