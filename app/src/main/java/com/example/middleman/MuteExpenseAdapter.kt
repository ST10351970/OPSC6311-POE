package com.example.middleman

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class MuteExpenseAdapter(private var expenses:List<Expense>,context: Context):
RecyclerView.Adapter<MuteExpenseAdapter.MuteExpenseViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MuteExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expense_item_design,parent,false)
        return MuteExpenseViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MuteExpenseViewHolder,
        position: Int
    ) {
        val expense = expenses[position]
        holder.expenseDate.text = expense.date.toString()
        holder.expenseAmount.text = "- "+currency(expense.amount)
        holder.category.text = expense.category.toString()
    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    class MuteExpenseViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)  {
        val expenseDate: TextView = itemView.findViewById(R.id.dateTextView)
        val expenseAmount: TextView = itemView.findViewById(R.id.amountTextView)
        val category: TextView = itemView.findViewById(R.id.groceriesTextView)
        val moreButton: ImageView = itemView.findViewById(R.id.moreImageView)
    }
    fun currency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(amount)
    }
}