package com.example.jalsanchaytracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RainfallDao {
    @Query("SELECT * FROM rainfall_data ORDER BY date DESC")
    fun getAllRainfallData(): Flow<List<RainfallEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRainfall(rainfall: RainfallEntity)

    @Delete
    suspend fun deleteRainfall(rainfall: RainfallEntity)

    @Query("SELECT SUM(waterSavedLiters) FROM rainfall_data")
    fun getTotalWaterSaved(): Flow<Double?>
}
