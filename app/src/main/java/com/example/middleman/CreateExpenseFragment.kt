package com.example.middleman

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import com.example.middleman.databinding.FragmentCreateExpenseBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CreateExpenseFragment : Fragment() {

    private lateinit var createExpenseBinding: FragmentCreateExpenseBinding
    private lateinit var expenseDatabaseHelper: ExpenseDatabaseHelper
    private lateinit var expenseCategoriesSpinner: Spinner
    private lateinit var categoryDatabaseHelper: CategoryDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        createExpenseBinding = FragmentCreateExpenseBinding.inflate(layoutInflater,container,false)
        return createExpenseBinding.root
        expenseDatabaseHelper = ExpenseDatabaseHelper(createExpenseBinding.root.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryDatabaseHelper = CategoryDatabaseHelper(createExpenseBinding.root.context)
        //initialising the expense database helper
        expenseDatabaseHelper = ExpenseDatabaseHelper(createExpenseBinding.root.context)
        //image must be invisible at the start
        createExpenseBinding.expenseImageView.visibility = View.INVISIBLE
        //button to go the create income fragment
        createExpenseBinding.IncomeButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_createExpenseFragment_to_createIncomeFragment)
        }

        val calendarDialogBox = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            calendarDialogBox.set(Calendar.YEAR,year)
            calendarDialogBox.set(Calendar.MONTH,month)
            calendarDialogBox.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            pickDate(calendarDialogBox)

        }
        createExpenseBinding.calenderButton.setOnClickListener {
            DatePickerDialog(createExpenseBinding.root.context,dateBox,
                calendarDialogBox.get(Calendar.YEAR),
                calendarDialogBox.get(Calendar.MONTH),
                calendarDialogBox.get(Calendar.DAY_OF_MONTH)).show()

        }

        //time picker button
        createExpenseBinding.timeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Launch TimePickerDialog
            val timePickerDialog = TimePickerDialog(createExpenseBinding.root.context, { _, selectedHour, selectedMinute ->
                val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                createExpenseBinding.timeEditText.text = time
            }, hour, minute, true) // true for 24-hour format

            timePickerDialog.show()
        }

        //categories spinner code
        val listOfCategories = mutableListOf<String>()

        val categoryObj = categoryDatabaseHelper.returnCategories()
        var i = 0
        while(i < categoryObj.size){
            listOfCategories.add(categoryObj[i].categoryTitle)
            i++
        }
        expenseCategoriesSpinner = createExpenseBinding.categoriesSpinner
        val arrayAdapter = ArrayAdapter(createExpenseBinding.root.context, android.R.layout.simple_spinner_item,listOfCategories)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        expenseCategoriesSpinner.adapter = arrayAdapter

        expenseCategoriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                createExpenseBinding.categoryTextView.text = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        //image capture code
        createExpenseBinding.pictureButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cameraIntent)
        }

        //save expense button
        createExpenseBinding.saveButton.setOnClickListener {
            val title = createExpenseBinding.titleEditText.text.toString()
            val category = createExpenseBinding.categoryTextView.text.toString()
            val description = createExpenseBinding.descriptionEditText.text.toString()
            val date = createExpenseBinding.dateEditText.text.toString()
            val time = createExpenseBinding.timeEditText.text.toString()
            val amount = createExpenseBinding.amountEditText.text.toString()
            val expenseImage = imageViewToByteArray(createExpenseBinding.expenseImageView)

            if(amount !=""){
                val expenseCreated = Expense(0,title,description,date,time,amount.toDouble(),category,expenseImage)
                expenseDatabaseHelper.createExpenseEntry(expenseCreated)
                Toast.makeText(createExpenseBinding.root.context,"Expense Created Successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(createExpenseBinding.root.context,"Please fill in all details", Toast.LENGTH_SHORT).show()
            }


        }

    }
    private fun pickDate(calendar: Calendar){
        val dateFormat = "yyyy-MM-dd"
        val simple =SimpleDateFormat(dateFormat, Locale.UK)
        createExpenseBinding.dateEditText.text = simple.format(calendar.time)
    }
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result?.data != null) {
                    val bitmap = result.data?.extras?.get("data") as Bitmap
                    createExpenseBinding.expenseImageView.visibility = View.VISIBLE
                    createExpenseBinding.expenseImageView.setImageBitmap(bitmap)
                }
            }
        }
    fun imageViewToByteArray(imageView: ImageView): ByteArray {

        val drawable = imageView.drawable
        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }


}