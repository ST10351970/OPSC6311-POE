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
import com.example.middleman.databinding.FragmentCreateIncomeBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CreateIncomeFragment : Fragment() {

    private lateinit var createIncomeBinding: FragmentCreateIncomeBinding
    private lateinit var incomeCategoriesSpinner: Spinner
    private lateinit var incomeDatabaseHelper: IncomeDatabaseHelper
    private lateinit var incomeDatabase: IncomeDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        createIncomeBinding = FragmentCreateIncomeBinding.inflate(layoutInflater,container,false)
        return createIncomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        incomeDatabase = IncomeDatabase(createIncomeBinding.root.context)

        createIncomeBinding.ExpenseButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_createIncomeFragment_to_createExpenseFragment)
        }
        incomeDatabaseHelper = IncomeDatabaseHelper(createIncomeBinding.root.context)
        //date picker button
        val calendarDialogBox = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            calendarDialogBox.set(Calendar.YEAR,year)
            calendarDialogBox.set(Calendar.MONTH,month)
            calendarDialogBox.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            pickDate(calendarDialogBox)

        }
        createIncomeBinding.calenderButton.setOnClickListener {
            DatePickerDialog(createIncomeBinding.root.context,dateBox,
                calendarDialogBox.get(Calendar.YEAR),
                calendarDialogBox.get(Calendar.MONTH),
                calendarDialogBox.get(Calendar.DAY_OF_MONTH)).show()

        }

        // time picker button
        createIncomeBinding.timeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Launch TimePickerDialog
            val timePickerDialog = TimePickerDialog(createIncomeBinding.root.context, { _, selectedHour, selectedMinute ->
                val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                createIncomeBinding.timeEditText.text = time
            }, hour, minute, true) // true for 24-hour format

            timePickerDialog.show()
        }
        //categories spinner


        val listOfCategories = mutableListOf<String>()

        val categoryObj = incomeDatabase.returnCategories()
        var i = 0
        while(i < categoryObj.size){
            listOfCategories.add(categoryObj[i].description)
            i++
        }
        incomeCategoriesSpinner = createIncomeBinding.categoriesSpinner
        val arrayAdapter = ArrayAdapter(createIncomeBinding.root.context, android.R.layout.simple_spinner_item,listOfCategories)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        incomeCategoriesSpinner.adapter = arrayAdapter

        incomeCategoriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                createIncomeBinding.categoryTextView.text = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //photo button
        createIncomeBinding.pictureButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cameraIntent)
        }

        //save button
        createIncomeBinding.saveButton.setOnClickListener {
            val title = createIncomeBinding.titleEditText.text.toString()
            val category = createIncomeBinding.categoryTextView.text.toString()
            val description = createIncomeBinding.descriptionEditText.text.toString()
            val date = createIncomeBinding.dateEditText.text.toString()
            val time = createIncomeBinding.timeEditText.text.toString()
            val amount = createIncomeBinding.amountEditText.text.toString()
            val expenseImage = imageViewToByteArray(createIncomeBinding.incomeImageView)

            if(amount !=""){
                val incomeCreated = Income(0,title,description,date,time,amount.toDouble(),category,expenseImage)
                incomeDatabaseHelper.createIncomeEntry(incomeCreated)


                Toast.makeText(createIncomeBinding.root.context,"Income Created Successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(createIncomeBinding.root.context,"Please fill in all details", Toast.LENGTH_SHORT).show()
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

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result?.data != null) {
                    val bitmap = result.data?.extras?.get("data") as Bitmap
                    createIncomeBinding.incomeImageView.visibility = View.VISIBLE
                    createIncomeBinding.incomeImageView.setImageBitmap(bitmap)
                }
            }
        }
    private fun pickDate(calendar: Calendar){
        val dateFormat = "yyyy-MM-dd"
        val simple =SimpleDateFormat(dateFormat, Locale.UK)
        createIncomeBinding.dateEditText.text = simple.format(calendar.time)
    }


}