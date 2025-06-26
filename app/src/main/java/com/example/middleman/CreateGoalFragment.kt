package com.example.middleman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.middleman.databinding.FragmentCreateGoalBinding


class CreateGoalFragment : Fragment() {

    private lateinit var createGoalBinding: FragmentCreateGoalBinding
    private lateinit var gaolDatabaseHelper: GaolDatabaseHelper
    private lateinit var categoriesSpinner: Spinner
    private lateinit var incomeDatabase: IncomeDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        createGoalBinding = FragmentCreateGoalBinding.inflate(layoutInflater,container,false)
        return createGoalBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        incomeDatabase = IncomeDatabase(createGoalBinding.root.context)
        gaolDatabaseHelper = GaolDatabaseHelper(createGoalBinding.root.context)
        // spinner for categories
        val listOfCategories = mutableListOf<String>()

        val incomeCategories = incomeDatabase.returnCategories()
        var i = 0
        while(i < incomeCategories.size){
            listOfCategories.add(incomeCategories[i].description)
            i++
        }

        categoriesSpinner = createGoalBinding.categoriesSpinner
        val arrayAdapter = ArrayAdapter(createGoalBinding.root.context, android.R.layout.simple_spinner_item,listOfCategories)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoriesSpinner.adapter = arrayAdapter

        categoriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                createGoalBinding.categoryTextView.text = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        createGoalBinding.saveButton.setOnClickListener {
            val category = createGoalBinding.categoryTextView.text.toString()
            val description = createGoalBinding.descriptionGoalEditText.text.toString()
            val targetAmount = createGoalBinding.totalAmountGoalEditText.editableText.toString().toDouble()
            val accumulated = createGoalBinding.accumulatedAmountEditText.text.toString().toDouble()

            val goal = Goal(0,category,targetAmount,accumulated)
            gaolDatabaseHelper.createNewGoal(goal)
            Toast.makeText(createGoalBinding.root.context,"Goal created successfully",Toast.LENGTH_SHORT).show()

            it.findNavController().navigate(R.id.action_createGoalFragment_to_goalsFragment)
        }
    }


}