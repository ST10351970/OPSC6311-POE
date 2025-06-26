package com.example.middleman

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.middleman.UserDatabase
import com.example.middleman.UserRepository
import com.example.middleman.databinding.ActivityLoginBinding
import com.example.middleman.PreferencesManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userRepository: UserRepository
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database and repository
        val database = UserDatabase.getDatabase(this)
        userRepository = UserRepository(database.userDao())
        preferencesManager = PreferencesManager(this)

        // Check if user is already logged in
        if (preferencesManager.isLoggedIn()) {
            navigateToMainActivity()
            return
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.loginBtn.setOnClickListener {
            val email = binding.emailInputL.text.toString().trim()
            val password = binding.passwordInputL.text.toString().trim()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }

        binding.signUpRedirectBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        when {
            email.isEmpty() -> {
                binding.emailInputL.error = "Email is required"
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailInputL.error = "Please enter a valid email"
                return false
            }
            password.isEmpty() -> {
                binding.passwordInputL.error = "Password is required"
                return false
            }
        }
        return true
    }

    private fun loginUser(email: String, password: String) {
        binding.loginBtn.isEnabled = false

        lifecycleScope.launch {
            try {
                val result = userRepository.loginUser(email, password)

                result.onSuccess { user ->
                    // Save user session
                    preferencesManager.saveUserSession(
                        user.id,
                        user.name,
                        user.email
                    )

                    Toast.makeText(
                        this@LoginActivity,
                        "Login successful!",
                        Toast.LENGTH_SHORT
                    ).show()

                    navigateToMainActivity()
                }.onFailure { exception ->
                    Toast.makeText(
                        this@LoginActivity,
                        exception.message ?: "Login failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    "An error occurred: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.loginBtn.isEnabled = true
            }
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}