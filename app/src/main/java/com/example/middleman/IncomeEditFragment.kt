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
import com.example.middleman.databinding.FragmentIncomeEditBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class IncomeEditFragment : Fragment() {

    private lateinit var incomeEditBinding: FragmentIncomeEditBinding
    private lateinit var incomeDatabaseHelper: IncomeDatabaseHelper
    private lateinit var incomeCategoriesSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        incomeEditBinding = FragmentIncomeEditBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return incomeEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        incomeDatabaseHelper = IncomeDatabaseHelper(incomeEditBinding.root.context)
        val income = incomeDatabaseHelper.getIncomeById(incomeId)

        incomeEditBinding.categoriesSpinner.setSelection(0)
        incomeEditBinding.titleEditText.setText(income.title)
        incomeEditBinding.timeEditText.text = income.time.toString()
        incomeEditBinding.dateEditText.text = income.date.toString()
        incomeEditBinding.descriptionEditText.setText(income.description.toString())
        incomeEditBinding.amountEditText.setText(income.amount.toString())
        incomeEditBinding.incomeImageView.setImageBitmap(byteArrayToBitmap( income.picture))


        incomeEditBinding.deleteButton.setOnClickListener {
            incomeDatabaseHelper.deleteIncome(incomeId)
            it.findNavController().navigate(R.id.action_incomeEditFragment_to_incomeReportFragment)

            Toast.makeText(incomeEditBinding.root.context,"Income Deleted", Toast.LENGTH_SHORT).show()
        }

        //date picker button
        val calendarDialogBox = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            calendarDialogBox.set(Calendar.YEAR,year)
            calendarDialogBox.set(Calendar.MONTH,month)
            calendarDialogBox.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            pickDate(calendarDialogBox)

        }
        incomeEditBinding.calenderButton.setOnClickListener {
            DatePickerDialog(incomeEditBinding.root.context,dateBox,
                calendarDialogBox.get(Calendar.YEAR),
                calendarDialogBox.get(Calendar.MONTH),
                calendarDialogBox.get(Calendar.DAY_OF_MONTH)).show()

        }

        // time picker button
        incomeEditBinding.timeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Launch TimePickerDialog
            val timePickerDialog = TimePickerDialog(incomeEditBinding.root.context, { _, selectedHour, selectedMinute ->
                val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                incomeEditBinding.timeEditText.text = time
            }, hour, minute, true) // true for 24-hour format

            timePickerDialog.show()
        }
        //categories spinner
        val listOfCategories = mutableListOf<String>("Food","Electricity","Water","Tutoring","Insurance","Other")
        incomeCategoriesSpinner = incomeEditBinding.categoriesSpinner
        val arrayAdapter = ArrayAdapter(incomeEditBinding.root.context, android.R.layout.simple_spinner_item,listOfCategories)
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
                incomeEditBinding.categoryTextView.text = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //photo button
        incomeEditBinding.pictureButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cameraIntent)
        }

        //save button
        incomeEditBinding.saveButton.setOnClickListener {
            val title = incomeEditBinding.titleEditText.text.toString()
            val category = incomeEditBinding.categoryTextView.text.toString()
            val description = incomeEditBinding.descriptionEditText.text.toString()
            val date = incomeEditBinding.dateEditText.text.toString()
            val time = incomeEditBinding.timeEditText.text.toString()
            val amount = incomeEditBinding.amountEditText.text.toString()
            val expenseImage = imageViewToByteArray(incomeEditBinding.incomeImageView)

            if(amount !=""){
                val incomeCreated = Income(0,title,description,date,time,amount.toDouble(),category,expenseImage)
                incomeDatabaseHelper.updateExpenseEntry(incomeCreated,incomeId)


                Toast.makeText(incomeEditBinding.root.context,"Income Updated Successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(incomeEditBinding.root.context,"Please fill in all details", Toast.LENGTH_SHORT).show()
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
                    incomeEditBinding.incomeImageView.visibility = View.VISIBLE
                    incomeEditBinding.incomeImageView.setImageBitmap(bitmap)
                }
            }
        }
    private fun pickDate(calendar: Calendar){
        val dateFormat = "yyyy-MM-dd"
        val simple =SimpleDateFormat(dateFormat, Locale.UK)
        incomeEditBinding.dateEditText.text = simple.format(calendar.time)
    }
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}