package com.example.jalsanchaytracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_usage")
data class WaterUsageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val amountLiters: Double,
    val category: String
)
