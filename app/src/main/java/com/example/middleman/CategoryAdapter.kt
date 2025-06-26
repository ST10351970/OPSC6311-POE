package com.example.middleman

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private var categories: List<Category>,context: Context):
RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item_design,parent,false)
        return CategoryViewHolder(view)
    }

    fun updateItems(newItems:List<Category>){
        categories = newItems
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int
    ) {
        val category = categories[position]
        holder.categoryTextView.text = category.categoryTitle

        holder.nextButton.setOnClickListener {
            expenseCategoryId = category.categoryId
            it.findNavController().navigate(R.id.action_allCategoriesFragment_to_editExpenseCategoryFragment)

        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoryViewHolder(categoryItemView: View): RecyclerView.ViewHolder(categoryItemView) {
        val categoryTextView: TextView = categoryItemView.findViewById(R.id.newCategoryTextView)
        val nextButton: ImageView = categoryItemView.findViewById(R.id.nextExpenseCategoryButton)
    }



}