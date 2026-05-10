package com.example.jalsanchaytracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rainfall_data")
data class RainfallEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val rainfallMm: Double,
    val waterSavedLiters: Double
)
