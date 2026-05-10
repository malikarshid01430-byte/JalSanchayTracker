package com.example.jalsanchaytracker.repository

import com.example.jalsanchaytracker.data.RainfallDao
import com.example.jalsanchaytracker.data.RainfallEntity
import kotlinx.coroutines.flow.Flow

class RainfallRepository(private val rainfallDao: RainfallDao) {
    val allRainfallData: Flow<List<RainfallEntity>> = rainfallDao.getAllRainfallData()
    val totalWaterSaved: Flow<Double?> = rainfallDao.getTotalWaterSaved()

    suspend fun insert(rainfall: RainfallEntity) {
        rainfallDao.insertRainfall(rainfall)
    }

    suspend fun delete(rainfall: RainfallEntity) {
        rainfallDao.deleteRainfall(rainfall)
    }
}
