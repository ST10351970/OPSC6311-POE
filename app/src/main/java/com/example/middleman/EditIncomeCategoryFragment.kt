package com.example.middleman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.middleman.databinding.FragmentEditIncomeCategoryBinding
import com.example.middleman.databinding.FragmentIncomeCategoriesBinding


class EditIncomeCategoryFragment : Fragment() {

    private lateinit var binding: FragmentEditIncomeCategoryBinding
    private lateinit var incomeDatabase: IncomeDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditIncomeCategoryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        incomeDatabase = IncomeDatabase(binding.root.context)
        binding.descriptionCategoryEditText.setText(incomeDatabase.getCategoryById(incomeCategoryId).description)

        binding.saveButton.setOnClickListener {
            val category = binding.descriptionCategoryEditText.text.toString()
            val categoryObj = IncomeCategory(0,category)
            incomeDatabase.updateCategory(categoryObj,incomeCategoryId)
            Toast.makeText(binding.root.context,"Category Updated Successfully", Toast.LENGTH_SHORT).show()
            it.findNavController().navigate(R.id.action_editIncomeCategoryFragment_to_incomeCategoriesFragment)
        }
        binding.cancelButton.setOnClickListener {
            incomeDatabase.deleteCategory(incomeCategoryId)
            Toast.makeText(binding.root.context,"Category Deleted", Toast.LENGTH_SHORT).show()
            it.findNavController().navigate(R.id.action_editIncomeCategoryFragment_to_incomeCategoriesFragment)

        }
    }


}