package com.example.middleman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.middleman.databinding.FragmentCreateIncomeCategoryBinding


class CreateIncomeCategoryFragment : Fragment() {


    private lateinit var createBinding: FragmentCreateIncomeCategoryBinding
    private lateinit var incomeCategoriesDatabase: IncomeDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        createBinding = FragmentCreateIncomeCategoryBinding.inflate(layoutInflater,container,false)
        return createBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        incomeCategoriesDatabase = IncomeDatabase(createBinding.root.context)

        createBinding.saveButton.setOnClickListener {
            val category = createBinding.descriptionCategoryEditText.text.toString()
            val categoryObj = IncomeCategory(0,category)

            incomeCategoriesDatabase.createNewCategory(categoryObj)
            Toast.makeText(createBinding.root.context,"Category Added", Toast.LENGTH_SHORT).show()
            it.findNavController().navigate(R.id.action_createIncomeCategoryFragment_to_incomeCategoriesFragment)

        }

        createBinding.cancelButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_createIncomeCategoryFragment_to_incomeCategoriesFragment)
        }
    }


}