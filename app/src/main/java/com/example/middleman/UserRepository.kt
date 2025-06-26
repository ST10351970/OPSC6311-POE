package com.example.middleman

import com.example.middleman.User
import com.example.middleman.UserDao

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: User): Result<User> {
        return try {
            // Check if user already exists
            val existingUser = userDao.getUserByEmail(user.email)
            if (existingUser != null) {
                Result.failure(Exception("User with this email already exists"))
            } else {
                val userId = userDao.insertUser(user)
                Result.success(user.copy(id = userId.toInt()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val user = userDao.loginUser(email, password)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(userId: Int): User? {
        return try {
            userDao.getUserById(userId)
        } catch (e: Exception) {
            null
        }
    }
}