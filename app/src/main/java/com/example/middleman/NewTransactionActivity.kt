package com.example.middleman

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.middleman.databinding.ActivityNewTransactionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewTransactionActivity : AppCompatActivity() {

    private lateinit var newTransactionsBinding: ActivityNewTransactionBinding
    private lateinit var bottomNavigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        newTransactionsBinding = ActivityNewTransactionBinding.inflate(layoutInflater)
        setContentView(newTransactionsBinding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bottomNavigation = newTransactionsBinding.bottomNavigationView
        bottomNavigation.selectedItemId = R.id.bottom_addBtn
        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){

                R.id.bottom_addBtn ->{
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
                    startActivity(Intent(this, DashboardActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }

                else -> false
            }

        }


    }
}