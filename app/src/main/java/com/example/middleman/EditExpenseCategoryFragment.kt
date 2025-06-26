package com.example.middleman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.middleman.databinding.FragmentEditExpenseCategoryBinding


class EditExpenseCategoryFragment : Fragment() {

    private lateinit var binding: FragmentEditExpenseCategoryBinding
    private lateinit var categoryDatabaseHelper: CategoryDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditExpenseCategoryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryDatabaseHelper = CategoryDatabaseHelper(binding.root.context)

        binding.descriptionCategoryEditText.setText(categoryDatabaseHelper.getCategoryById(expenseCategoryId).categoryTitle)

        binding.saveButton.setOnClickListener {
            val category = binding.descriptionCategoryEditText.text.toString()
            val categoryObj = Category(0,category)
            categoryDatabaseHelper.updateCategory(categoryObj,expenseCategoryId)
            Toast.makeText(binding.root.context,"Category Updated Successfully", Toast.LENGTH_SHORT).show()
            it.findNavController().navigate(R.id.action_editExpenseCategoryFragment_to_allCategoriesFragment)
        }
        binding.cancelButton.setOnClickListener {
            categoryDatabaseHelper.deleteCategory(expenseCategoryId)
            Toast.makeText(binding.root.context,"Category Deleted", Toast.LENGTH_SHORT).show()
            it.findNavController().navigate(R.id.action_editExpenseCategoryFragment_to_allCategoriesFragment)
        }
    }

}