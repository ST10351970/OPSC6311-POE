package com.example.middleman

data class Expense(val expenseId:Int,val title:String,val description:String,val date: String,
val time:String, val amount:Double,val category:String,val picture: ByteArray)
