package com.example.middleman

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.middleman.databinding.ActivityEditBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class EditActivity : AppCompatActivity() {

    private lateinit var editBinding: ActivityEditBinding
    private lateinit var bottomNavigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        editBinding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(editBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bottomNavigation = editBinding.bottomNavigationView
        bottomNavigation.selectedItemId = R.id.bottom_editBtn
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