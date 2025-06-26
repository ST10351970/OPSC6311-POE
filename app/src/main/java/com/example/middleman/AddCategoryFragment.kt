package com.example.middleman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.middleman.databinding.FragmentAddCategoryBinding


class AddCategoryFragment : Fragment() {

    private lateinit var addCategoryBinding: FragmentAddCategoryBinding
    private lateinit var categoryDatabaseHelper: CategoryDatabaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addCategoryBinding = FragmentAddCategoryBinding.inflate(layoutInflater,container,false)
        return addCategoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryDatabaseHelper = CategoryDatabaseHelper(addCategoryBinding.root.context)

        addCategoryBinding.saveButton.setOnClickListener {
            val category = addCategoryBinding.descriptionCategoryEditText.text.toString()

            val categoryObject = Category(0,category)
            categoryDatabaseHelper.createNewCategory(categoryObject)

            Toast.makeText(addCategoryBinding.root.context,"Category Saved", Toast.LENGTH_SHORT).show()
            it.findNavController().navigate(R.id.action_addCategoryFragment_to_allCategoriesFragment)


        }

    }

}