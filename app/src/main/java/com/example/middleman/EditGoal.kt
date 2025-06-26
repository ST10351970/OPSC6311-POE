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
import com.example.middleman.databinding.FragmentEditGoalBinding


class EditGoal : Fragment() {

    private lateinit var editGoalBinding: FragmentEditGoalBinding
    private lateinit var gaolDatabaseHelper: GaolDatabaseHelper
    private lateinit var categoriesSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editGoalBinding = FragmentEditGoalBinding.inflate(layoutInflater,container,false)
        return editGoalBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gaolDatabaseHelper = GaolDatabaseHelper(editGoalBinding.root.context)
        //setting the initial values
        val goal = gaolDatabaseHelper.getGoalById(goalId)
        editGoalBinding.categoryTextView.text = goal.category
        editGoalBinding.totalAmountGoalEditText.setText(goal.totalAmount.toString())
        editGoalBinding.accumulatedAmountEditText.setText(goal.currentAmount.toString())




        // spinner for categories
        val listOfCategories = mutableListOf<String>("✈ Vacation"," \uD83D\uDE99 Car","\uD83D\uDC54 Clothes","\uD83D\uDCBB Laptop","Accessories","Other")
        categoriesSpinner = editGoalBinding.categoriesSpinner
        val arrayAdapter = ArrayAdapter(editGoalBinding.root.context, android.R.layout.simple_spinner_item,listOfCategories)
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
                editGoalBinding.categoryTextView.text = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        editGoalBinding.saveButton.setOnClickListener {
            val category = editGoalBinding.categoryTextView.text.toString()
            val targetAmount = editGoalBinding.totalAmountGoalEditText.editableText.toString().toDouble()
            val accumulated = editGoalBinding.accumulatedAmountEditText.text.toString().toDouble()

            val goal = Goal(0,category,targetAmount,accumulated)
            gaolDatabaseHelper.updateGoal(goal,goalId)

            if(accumulated >= targetAmount){
                Toast.makeText(editGoalBinding.root.context,"Congratulations \uD83C\uDF89\uD83E\uDD73. Goal Completed",
                    Toast.LENGTH_SHORT).show()
                it.findNavController().navigate(R.id.action_editGoal_to_gameFragment)
            }else{
                Toast.makeText(editGoalBinding.root.context,"Goal updated successfully",Toast.LENGTH_SHORT).show()
                it.findNavController().navigate(R.id.action_editGoal_to_goalsFragment)
            }


        }
        editGoalBinding.deleteButton.setOnClickListener {
            gaolDatabaseHelper.deleteGoal(goalId)
            Toast.makeText(editGoalBinding.root.context,"Goal Deleted",Toast.LENGTH_SHORT).show()
        }
    }


}