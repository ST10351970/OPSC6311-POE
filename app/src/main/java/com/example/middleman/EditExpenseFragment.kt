package com.example.middleman

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.middleman.databinding.FragmentEditExpenseBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.exp


class EditExpenseFragment : Fragment() {

    private lateinit var editExpenseBinding: FragmentEditExpenseBinding
    private lateinit var expenseDatabaseHelper: ExpenseDatabaseHelper
    private lateinit var expenseCategoriesSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //setting initial values
        editExpenseBinding = FragmentEditExpenseBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return editExpenseBinding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseDatabaseHelper = ExpenseDatabaseHelper(editExpenseBinding.root.context)
        val expense = expenseDatabaseHelper.getExpenseById(userId)

        editExpenseBinding.categoriesSpinner.setSelection(0)
        editExpenseBinding.titleEditText.setText(expense.title)
        editExpenseBinding.timeEditText.text = expense.time.toString()
        editExpenseBinding.dateEditText.text = expense.date.toString()
        editExpenseBinding.descriptionEditText.setText(expense.description.toString())
        editExpenseBinding.amountEditText.setText(expense.amount.toString())
        editExpenseBinding.expenseImageView.setImageBitmap(byteArrayToBitmap( expense.picture))

        //initialising the expense database helper
        expenseDatabaseHelper = ExpenseDatabaseHelper(editExpenseBinding.root.context)
        //image must be invisible at the start

        //button to delete an expense entry
        editExpenseBinding.deleteButton.setOnClickListener {
            expenseDatabaseHelper.deleteExpense(userId)
            it.findNavController().navigate(R.id.action_editExpenseFragment_to_expenseReportFragment)
            Toast.makeText(editExpenseBinding.root.context,"Expense Transaction deleted", Toast.LENGTH_SHORT).show()
        }

        val calendarDialogBox = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            calendarDialogBox.set(Calendar.YEAR,year)
            calendarDialogBox.set(Calendar.MONTH,month)
            calendarDialogBox.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            pickDate(calendarDialogBox)

        }
        editExpenseBinding.calenderButton.setOnClickListener {
            DatePickerDialog(editExpenseBinding.root.context,dateBox,
                calendarDialogBox.get(Calendar.YEAR),
                calendarDialogBox.get(Calendar.MONTH),
                calendarDialogBox.get(Calendar.DAY_OF_MONTH)).show()

        }

        //time picker button
        editExpenseBinding.timeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Launch TimePickerDialog
            val timePickerDialog = TimePickerDialog(editExpenseBinding.root.context, { _, selectedHour, selectedMinute ->
                val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                editExpenseBinding.timeEditText.text = time
            }, hour, minute, true) // true for 24-hour format

            timePickerDialog.show()
        }

        //categories spinner code
        val listOfCategories = mutableListOf<String>("Food","Electricity","Water","Tutoring","Insurance","Other")
        expenseCategoriesSpinner = editExpenseBinding.categoriesSpinner
        val arrayAdapter = ArrayAdapter(editExpenseBinding.root.context, android.R.layout.simple_spinner_item,listOfCategories)
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
                editExpenseBinding.categoryTextView.text = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        //image capture code
        editExpenseBinding.pictureButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cameraIntent)
        }

        //save expense button
        editExpenseBinding.saveButton.setOnClickListener {
            val title = editExpenseBinding.titleEditText.text.toString()
            val category = editExpenseBinding.categoryTextView.text.toString()
            val description = editExpenseBinding.descriptionEditText.text.toString()
            val date = editExpenseBinding.dateEditText.text.toString()
            val time = editExpenseBinding.timeEditText.text.toString()
            val amount = editExpenseBinding.amountEditText.text.toString()
            val expenseImage = imageViewToByteArray(editExpenseBinding.expenseImageView)

            if(amount !=""){
                val expenseCreated = Expense(0,title,description,date,time,amount.toDouble(),category,expenseImage)
                expenseDatabaseHelper.updateExpenseEntry(expenseCreated,userId)
                Toast.makeText(editExpenseBinding.root.context,"Expense Updated Successfully", Toast.LENGTH_SHORT).show()
                it.findNavController().navigate(R.id.action_editExpenseFragment_to_expenseReportFragment)
            }else{
                Toast.makeText(editExpenseBinding.root.context,"Please fill in all details", Toast.LENGTH_SHORT).show()
            }


        }

    }
    private fun pickDate(calendar: Calendar){
        val dateFormat = "yyyy-MM-dd"
        val simple =SimpleDateFormat(dateFormat, Locale.UK)
        editExpenseBinding.dateEditText.text = simple.format(calendar.time)
    }
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result?.data != null) {
                    val bitmap = result.data?.extras?.get("data") as Bitmap
                    editExpenseBinding.expenseImageView.visibility = View.VISIBLE
                    editExpenseBinding.expenseImageView.setImageBitmap(bitmap)
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
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }


}