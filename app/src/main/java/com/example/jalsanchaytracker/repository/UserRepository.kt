package com.example.jalsanchaytracker.repository

import android.util.Log
import com.example.jalsanchaytracker.data.UserDao
import com.example.jalsanchaytracker.data.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val user: Flow<UserEntity?> = userDao.getUser()

    suspend fun insert(user: UserEntity) {
        try {
            userDao.insertUser(user)
        } catch (e: Exception) {
            Log.e("UserRepository", "Failed to insert user", e)
            throw e
        }
    }
}
