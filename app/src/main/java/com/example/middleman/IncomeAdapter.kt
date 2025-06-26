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

class IncomeAdapter(private var incomes: List<Income>,context: Context): RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder>() {

    class IncomeViewHolder(incomeItemView: View): RecyclerView.ViewHolder(incomeItemView) {
        val dateTextView:TextView = incomeItemView.findViewById(R.id.incomeDateTextView)
        val amountTextView:TextView = incomeItemView.findViewById(R.id.incomeAmountTextView)
        val categoryText: TextView = incomeItemView.findViewById(R.id.incomeCategoryTextView)
        val editButton: ImageView = incomeItemView.findViewById(R.id.EditImageView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IncomeAdapter.IncomeViewHolder {
        val incomeView = LayoutInflater.from(parent.context).inflate(R.layout.income_item_design,parent,false)
        return IncomeViewHolder(incomeView)
    }

    override fun onBindViewHolder(holder: IncomeAdapter.IncomeViewHolder, position: Int) {
        val income = incomes[position]
        holder.dateTextView.text = income.date.toString()
        holder.amountTextView.text = "+ ${currency(income.amount)}"
        holder.categoryText.text = income.category.toString()

        holder.editButton.setOnClickListener {
            incomeId = income.incomeId
            it.findNavController().navigate(R.id.action_incomeReportFragment_to_incomeEditFragment)
        }
    }

    override fun getItemCount(): Int {
        return incomes.size
    }
    fun currency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(amount)
    }
    fun refreshItems(latestItems:List<Income>){
        incomes = latestItems
        notifyDataSetChanged()
    }

}