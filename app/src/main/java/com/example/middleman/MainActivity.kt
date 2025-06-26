package com.example.middleman

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.middleman.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityMainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        //implementing view binding


        setContentView(mainActivityMainBinding.root)

        mainActivityMainBinding.loginButton.setOnClickListener {
            val startIntent = Intent(this, LoginActivity::class.java)
            startActivity(startIntent)
        }

        mainActivityMainBinding.tryButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)

        }

        // click listener for "How does MM work?" text
        mainActivityMainBinding.howItWorks.setOnClickListener {
            val intent = Intent(this, HowItWorks::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}