package com.example.jalsanchaytracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterUsageDao {
    @Query("SELECT * FROM water_usage ORDER BY date DESC")
    fun getAllUsage(): Flow<List<WaterUsageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsage(usage: WaterUsageEntity)

    @Query("SELECT SUM(amountLiters) FROM water_usage")
    fun getTotalUsage(): Flow<Double?>
}
