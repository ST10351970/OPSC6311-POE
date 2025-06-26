package com.example.middleman

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class IncomeDatabaseHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
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

    fun createIncomeEntry(income: Income){
        val database = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE,income.title)
            put(COLUMN_CATEGORY, income.category)
            put(COLUMN_DESCRIPTION,income.description)
            put(COLUMN_DATE,income.date)
            put(COLUMN_TIME,income.time)
            put(COLUMN_AMOUNT,income.amount)
            put(COLUMN_PICTURE,income.picture)
        }
        database.insert(TABLE_NAME,null,contentValues)
    }

    fun returnIncome():List<Income>{
        val database = readableDatabase
        val incomeRecords = mutableListOf<Income>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val records = database.rawQuery(selectQuery,null)

        while(records.moveToNext()){
            val incomeId = records.getInt(records.getColumnIndexOrThrow(COLUMN_ID))
            val title = records.getString(records.getColumnIndexOrThrow(COLUMN_TITLE))
            val description = records.getString(records.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val date = records.getString(records.getColumnIndexOrThrow(COLUMN_DATE))
            val time = records.getString(records.getColumnIndexOrThrow(COLUMN_TIME))
            val category = records.getString(records.getColumnIndexOrThrow(COLUMN_CATEGORY))
            val amount = records.getDouble(records.getColumnIndexOrThrow(COLUMN_AMOUNT))
            val image = records.getBlob(records.getColumnIndexOrThrow(COLUMN_PICTURE))

            val transaction = Income(incomeId,title,description,date,time,amount, category,image)
            incomeRecords.add(transaction)
        }
        records.close()
        database.close()
        return incomeRecords
    }
    fun getIncomeById(expenseId:Int):Income{
        val database = readableDatabase
        val incomeRecords = mutableListOf<Income>()
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $incomeId"
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

            val transaction = Income(expenseId,title,description,date,time,amount, category,image)
            incomeRecords.add(transaction)
        }
        records.close()
        database.close()
        return incomeRecords[0]
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

    fun updateExpenseEntry(income: Income,incomeId:Int){
        val database = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE,income.title)
            put(COLUMN_CATEGORY, income.category)
            put(COLUMN_DESCRIPTION,income.description)
            put(COLUMN_DATE,income.date)
            put(COLUMN_TIME,income.time)
            put(COLUMN_AMOUNT,income.amount)
            put(COLUMN_PICTURE,income.picture)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(incomeId.toString())
        database.update(TABLE_NAME,contentValues,whereClause,whereArgs)
        database.close()
    }

    fun deleteIncome(expenseId:Int){
        val database = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(expenseId.toString())
        database.delete(TABLE_NAME,whereClause,whereArgs)
        database.close()
    }


    fun incomeTotal():Double{
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

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        val destroyDatabase = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(destroyDatabase)
        onCreate(db)
    }

    companion object{
        private const val DATABASE_NAME = "Incomes"
        private const val DATABASE_VERSION = 5
        private const val TABLE_NAME = "IncomeTable"
        private const val COLUMN_ID = "IncomeId"
        private const val COLUMN_TITLE = "Title"
        private const val COLUMN_DESCRIPTION = "Description"
        private const val COLUMN_DATE = "Date"
        private const val COLUMN_CATEGORY = "Category"
        private const val COLUMN_TIME = "Time"
        private const val COLUMN_AMOUNT = "Amount"
        private const val COLUMN_PICTURE = "Picture"
    }
}