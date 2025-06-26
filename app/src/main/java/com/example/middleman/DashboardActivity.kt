package com.example.middleman

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.middleman.databinding.ActivityDashboardBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.NumberFormat
import java.util.Locale
import com.example.middleman.PreferencesManager

class DashboardActivity : AppCompatActivity() {
    private lateinit var dashboardBinding: ActivityDashboardBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var expenseDatabaseHelper: ExpenseDatabaseHelper
    private lateinit var incomeDatabaseHelper: IncomeDatabaseHelper
    private lateinit var muteExpenseAdapter: MuteExpenseAdapter
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(dashboardBinding.root)

        preferencesManager = PreferencesManager(this)

        // Check if user is logged in
        if (!preferencesManager.isLoggedIn()) {
            navigateToLogin()
            return
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        setupClickListeners()

        //initialising
        expenseDatabaseHelper = ExpenseDatabaseHelper(this)
        incomeDatabaseHelper = IncomeDatabaseHelper(this)

        //loading the stats
        dashboardBinding.totalIncomeHomeTextView.text = currency(incomeDatabaseHelper.incomeTotal())
        dashboardBinding.totalExpensesHomeTextView.text = currency(expenseDatabaseHelper.expenseTotal())
        val balance = currency(incomeDatabaseHelper.incomeTotal() - expenseDatabaseHelper.expenseTotal())
        dashboardBinding.balanceTextView.text = balance

        //pie chart
        var pieChart = dashboardBinding.pieChart

        val categoryTotals = incomeDatabaseHelper.getCategoryTotals()
        var entries = mutableListOf<PieEntry>(
            PieEntry(incomeDatabaseHelper.incomeTotal().toFloat(),"Total Income"),
            PieEntry(expenseDatabaseHelper.expenseTotal().toFloat(),"Total Expenses")
        )




        val dataSet = PieDataSet(entries, "Expenditure")
        dataSet.setColors(
            Color.GREEN,
            Color.RED,
        )
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 16f

        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.centerText = "Expenditure"
        pieChart.animateY(1000)

        //expenses recycler view
        muteExpenseAdapter = MuteExpenseAdapter(expenseDatabaseHelper.returnExpenses(),dashboardBinding.root.context)
        dashboardBinding.expenseReportRecyclerView.layoutManager = LinearLayoutManager(dashboardBinding.root.context)
        dashboardBinding.expenseReportRecyclerView.adapter = muteExpenseAdapter

        //bottom navigation code
        bottomNavigation = dashboardBinding.bottomNavigationView
        bottomNavigation.selectedItemId = R.id.bottom_homeBtn
        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.bottom_addBtn ->{
                    startActivity(Intent(this, NewTransactionActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.bottom_reportBtn ->{
                    startActivity(Intent(this, ReportActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.bottom_goalBtn ->{
                    startActivity(Intent(this, GoalsActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.bottom_editBtn ->{
                    startActivity(Intent(this, EditActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.bottom_homeBtn ->{
                    true
                }

                else -> false
            }

        }
        }
    private fun setupUI() {
        val userName = preferencesManager.getUserName()
        dashboardBinding.nameDisplay.text = "Welcome: $userName"
    }

    private fun setupClickListeners() {
        dashboardBinding.logoutBtn.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        preferencesManager.clearUserSession()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun currency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(amount)
    }
    }
