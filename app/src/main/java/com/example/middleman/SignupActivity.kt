package com.example.middleman

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.middleman.UserDatabase
import com.example.middleman.User
import com.example.middleman.UserRepository
import com.example.middleman.databinding.ActivitySignupBinding
import com.example.middleman.PreferencesManager
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var userRepository: UserRepository
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database and repository
        val database = UserDatabase.getDatabase(this)
        userRepository = UserRepository(database.userDao())
        preferencesManager = PreferencesManager(this)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.signupBtn.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            val confirmPassword = binding.confirmPasswordInput.text.toString().trim()

            if (validateInput(name, email, password, confirmPassword)) {
                registerUser(name, email, password)
            }
        }
    }

    private fun validateInput(name: String, email: String, password: String, confirmPassword: String): Boolean {
        when {
            name.isEmpty() -> {
                binding.nameInput.error = "Name is required"
                return false
            }
            email.isEmpty() -> {
                binding.emailInput.error = "Email is required"
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailInput.error = "Please enter a valid email"
                return false
            }
            password.isEmpty() -> {
                binding.passwordInput.error = "Password is required"
                return false
            }
            password.length < 6 -> {
                binding.passwordInput.error = "Password must be at least 6 characters"
                return false
            }
            confirmPassword.isEmpty() -> {
                binding.confirmPasswordInput.error = "Confirm password entry is required"
                return false
            }
            password != confirmPassword -> {
                binding.confirmPasswordInput.error = "Passwords do not match"
                return false
            }
        }
        return true
    }

    private fun registerUser(name: String, email: String, password: String) {
        binding.signupBtn.isEnabled = false

        lifecycleScope.launch {
            try {
                val user = User(name = name, email = email, password = password)
                val result = userRepository.registerUser(user)

                result.onSuccess { registeredUser ->
                    // Save user session
                    preferencesManager.saveUserSession(
                        registeredUser.id,
                        registeredUser.name,
                        registeredUser.email
                    )

                    Toast.makeText(
                        this@SignupActivity,
                        "Registration successful!",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Navigate to main activity or dashboard
                    startActivity(Intent(this@SignupActivity, DashboardActivity::class.java))
                    finish()
                }.onFailure { exception ->
                    Toast.makeText(
                        this@SignupActivity,
                        exception.message ?: "Registration failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@SignupActivity,
                    "An error occurred: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.signupBtn.isEnabled = true
            }
        }
    }
}