package com.example.jalsanchaytracker.repository

import android.util.Log
import com.example.jalsanchaytracker.data.WaterUsageDao
import com.example.jalsanchaytracker.data.WaterUsageEntity
import kotlinx.coroutines.flow.Flow

class WaterUsageRepository(private val waterUsageDao: WaterUsageDao) {
    val allUsage: Flow<List<WaterUsageEntity>> = waterUsageDao.getAllUsage()
    val totalUsage: Flow<Double?> = waterUsageDao.getTotalUsage()

    suspend fun insert(usage: WaterUsageEntity) {
        try {
            waterUsageDao.insertUsage(usage)
        } catch (e: Exception) {
            Log.e("WaterUsageRepository", "Failed to insert water usage", e)
            throw e
        }
    }
}
