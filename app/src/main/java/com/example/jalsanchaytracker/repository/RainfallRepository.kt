package com.example.jalsanchaytracker.repository

import android.util.Log
import com.example.jalsanchaytracker.data.RainfallDao
import com.example.jalsanchaytracker.data.RainfallEntity
import kotlinx.coroutines.flow.Flow

class RainfallRepository(private val rainfallDao: RainfallDao) {
    val allRainfallData: Flow<List<RainfallEntity>> = rainfallDao.getAllRainfallData()
    val totalWaterSaved: Flow<Double?> = rainfallDao.getTotalWaterSaved()

    suspend fun insert(rainfall: RainfallEntity) {
        try {
            rainfallDao.insertRainfall(rainfall)
        } catch (e: Exception) {
            Log.e("RainfallRepository", "Failed to insert rainfall data", e)
            throw e
        }
    }

    suspend fun delete(rainfall: RainfallEntity) {
        try {
            rainfallDao.deleteRainfall(rainfall)
        } catch (e: Exception) {
            Log.e("RainfallRepository", "Failed to delete rainfall data", e)
            throw e
        }
    }
}
