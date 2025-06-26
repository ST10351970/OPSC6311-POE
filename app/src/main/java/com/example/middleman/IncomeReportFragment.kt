package com.example.middleman

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.middleman.databinding.FragmentIncomeReportBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.NumberFormat
import java.util.Locale


class IncomeReportFragment : Fragment() {

    private lateinit var incomeReportBinding: FragmentIncomeReportBinding
    private lateinit var incomeAdapter: IncomeAdapter
    private lateinit var incomeDatabaseHelper: IncomeDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        incomeReportBinding = FragmentIncomeReportBinding.inflate(layoutInflater,container,false)
        return incomeReportBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        incomeReportBinding.expenseButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_incomeReportFragment_to_expenseReportFragment)
        }
        incomeDatabaseHelper = IncomeDatabaseHelper(incomeReportBinding.root.context)
        incomeAdapter = IncomeAdapter(incomeDatabaseHelper.returnIncome(),incomeReportBinding.root.context)

        incomeReportBinding.incomeReportRecyclerView.layoutManager = LinearLayoutManager(incomeReportBinding.root.context)
        incomeReportBinding.incomeReportRecyclerView.adapter = incomeAdapter

        // income report stats
        incomeReportBinding.totalIncomeTextView.text = "+ "+currency(incomeDatabaseHelper.incomeTotal())
        incomeReportBinding.numberOfIncomeTextView.text = incomeAdapter.itemCount.toString()

        var pieChart = incomeReportBinding.pieChart

        val categoryTotals = incomeDatabaseHelper.getCategoryTotals()
        /*
        * CategoryTotal(4000,Food),
        * CategoryTotal(2000,Transport),
        * CategoryTotal(4000, Maintenance)
        *
        * PieEntry(3000,"Food")
        * */
        var entries = mutableListOf<PieEntry>()
        var n = 0
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
        pieChart.centerText = "Income"
        pieChart.animateY(1000)
    }
    fun currency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(amount)
    }

    override fun onResume() {
        super.onResume()
        incomeAdapter.refreshItems(incomeDatabaseHelper.returnIncome())
    }


}