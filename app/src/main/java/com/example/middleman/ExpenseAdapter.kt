package com.example.middleman

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.exp

class ExpenseAdapter(private var expenses: List<Expense>,context: Context):
RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expense_item_design,parent,false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.expenseDate.text = expense.date.toString()
        holder.expenseAmount.text = "- "+currency(expense.amount)
        holder.category.text = expense.category.toString()
        holder.moreButton.setOnClickListener {
            userId = expense.expenseId
            it.findNavController().navigate(R.id.action_expenseReportFragment_to_editExpenseFragment)

        }
    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    class ExpenseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val expenseDate: TextView = itemView.findViewById(R.id.dateTextView)
        val expenseAmount: TextView = itemView.findViewById(R.id.amountTextView)
        val category: TextView = itemView.findViewById(R.id.groceriesTextView)
        val moreButton: ImageView = itemView.findViewById(R.id.moreImageView)

    }
    fun currency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(amount)
    }
    fun refreshItems(latestItems:List<Expense>){
        expenses = latestItems
        notifyDataSetChanged()
    }
}