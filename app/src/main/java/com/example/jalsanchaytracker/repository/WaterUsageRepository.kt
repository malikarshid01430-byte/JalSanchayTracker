package com.example.jalsanchaytracker.repository

import com.example.jalsanchaytracker.data.WaterUsageDao
import com.example.jalsanchaytracker.data.WaterUsageEntity
import kotlinx.coroutines.flow.Flow

class WaterUsageRepository(private val waterUsageDao: WaterUsageDao) {
    val allUsage: Flow<List<WaterUsageEntity>> = waterUsageDao.getAllUsage()
    val totalUsage: Flow<Double?> = waterUsageDao.getTotalUsage()

    suspend fun insert(usage: WaterUsageEntity) {
        waterUsageDao.insertUsage(usage)
    }
}
