package com.example.jalsanchaytracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val roofArea: Double = 0.0,
    val tankCapacity: Double = 0.0,
    val location: String = ""
)
