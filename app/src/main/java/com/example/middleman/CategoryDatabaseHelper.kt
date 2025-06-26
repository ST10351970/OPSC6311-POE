package com.example.middleman

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CategoryDatabaseHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_CATEGORY TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        val dropTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTable)
        onCreate(db)
    }

    fun createNewCategory(category: Category){
        val database = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_CATEGORY,category.categoryTitle)
        }
        database.insert(TABLE_NAME,null,contentValues)
    }
    fun getCategoryById(categoryId: Int): Category{
        val database = readableDatabase
        val categories = mutableListOf<Category>()
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $categoryId"
        val cursor = database.rawQuery(selectQuery,null)

        while(cursor.moveToNext()){
            val categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
            val categoryObj = Category(categoryId,category)
            categories.add(categoryObj)
        }
        cursor.close()
        database.close()
        return categories[0]
    }

    fun returnCategories():List<Category>{
        val database = readableDatabase
        val categories = mutableListOf<Category>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = database.rawQuery(selectQuery,null)

        while(cursor.moveToNext()){
            val categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
            val categoryObj = Category(categoryId,category)
            categories.add(categoryObj)
        }
        cursor.close()
        database.close()
        return categories
    }

    fun updateCategory(category: Category,categoryId: Int){
        val database = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_CATEGORY,category.categoryTitle)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(categoryId.toString())
        database.update(TABLE_NAME,contentValues,whereClause,whereArgs)
        database.close()

    }

    fun deleteCategory(categoryId: Int){
        val database = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(categoryId.toString())
        database.delete(TABLE_NAME,whereClause,whereArgs)
        database.close()
    }

    companion object{
        private const val DATABASE_NAME = "CategoryDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Categories"
        private const val COLUMN_ID =  "CategoryId"
        private const val COLUMN_CATEGORY = "CategoryTitle"
    }
}