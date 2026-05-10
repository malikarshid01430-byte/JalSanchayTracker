package com.example.jalsanchaytracker

import android.app.Application
import com.example.jalsanchaytracker.data.AppDatabase
import com.example.jalsanchaytracker.repository.RainfallRepository
import com.example.jalsanchaytracker.repository.UserRepository
import com.example.jalsanchaytracker.repository.WaterUsageRepository

class JalSanchayApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val rainfallRepository by lazy { RainfallRepository(database.rainfallDao()) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val waterUsageRepository by lazy { WaterUsageRepository(database.waterUsageDao()) }
}
