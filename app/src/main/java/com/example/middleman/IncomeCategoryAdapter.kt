package com.example.middleman

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class IncomeCategoryAdapter(private var incomeCategories: List<IncomeCategory>,context: Context):
RecyclerView.Adapter<IncomeCategoryAdapter.IncomeCategoryViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.income_category_design,parent,false)
        return IncomeCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: IncomeCategoryViewHolder, position: Int) {
        val category = incomeCategories[position]
        holder.incomeCategoryTextView.text = category.description.toString()

        holder.nextButton.setOnClickListener {
            incomeCategoryId = category.categoryId
            it.findNavController().navigate(R.id.action_incomeCategoriesFragment_to_editIncomeCategoryFragment)
        }
    }

    override fun getItemCount(): Int {
        return incomeCategories.size
    }
    fun updateItems(newItems:List<IncomeCategory>){
        incomeCategories = newItems
        notifyDataSetChanged()
    }

    class IncomeCategoryViewHolder(incomeCategoryItem: View): RecyclerView.ViewHolder(incomeCategoryItem) {

        val incomeCategoryTextView: TextView = incomeCategoryItem.findViewById(R.id.newIncomeDesCategoryTextView)
        val nextButton: ImageView = incomeCategoryItem.findViewById(R.id.nextIncomeCategoryButton)
    }


}