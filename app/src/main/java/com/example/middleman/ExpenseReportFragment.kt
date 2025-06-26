package com.example.middleman

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.middleman.databinding.FragmentExpenseReportBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.exp

class ExpenseReportFragment : Fragment() {

    private lateinit var expenseReportBinding: FragmentExpenseReportBinding
    private lateinit var expenseDatabaseHelper: ExpenseDatabaseHelper
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        expenseReportBinding = FragmentExpenseReportBinding.inflate(layoutInflater,container,false)
        return expenseReportBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expenseDatabaseHelper = ExpenseDatabaseHelper(expenseReportBinding.root.context)

        expenseAdapter = ExpenseAdapter(expenseDatabaseHelper.returnExpenses(),expenseReportBinding.root.context)
        expenseReportBinding.expenseReportRecyclerView.layoutManager = LinearLayoutManager(expenseReportBinding.root.context)
        expenseReportBinding.expenseReportRecyclerView.adapter = expenseAdapter

        expenseReportBinding.incomeButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_expenseReportFragment_to_incomeReportFragment)
        }

        // expense report stats
        expenseReportBinding.totalExpensesTextView.text = "- "+ currency(expenseDatabaseHelper.expenseTotal())
        expenseReportBinding.numberOfExpensesTextView.text = expenseAdapter.itemCount.toString()

        var pieChart = expenseReportBinding.pieChart

        val categoryTotals = expenseDatabaseHelper.getCategoryTotals()
        var entries = mutableListOf<PieEntry>()
        var n =0
        while (n < categoryTotals.count()){
            entries.add(PieEntry(categoryTotals[n].amount,categoryTotals[n].category))
            n++
        }



        val dataSet = PieDataSet(entries, "Categories")
        dataSet.setColors(
            Color.BLUE,
            Color.MAGENTA,
            Color.GREEN,
            Color.CYAN,
            Color.RED,
            Color.MAGENTA,
            Color.DKGRAY
        )
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 16f

        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.centerText = "Expenditure"
        pieChart.animateY(1000)

    }

    override fun onResume() {
        super.onResume()
        expenseAdapter.refreshItems(expenseDatabaseHelper.returnExpenses())
    }
    fun currency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(amount)
    }


}