package com.example.jalsanchaytracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jalsanchaytracker.data.RainfallEntity
import com.example.jalsanchaytracker.data.UserEntity
import com.example.jalsanchaytracker.data.WaterUsageEntity
import com.example.jalsanchaytracker.repository.RainfallRepository
import com.example.jalsanchaytracker.repository.UserRepository
import com.example.jalsanchaytracker.repository.WaterUsageRepository
import com.example.jalsanchaytracker.util.stateInWhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.graphics.Color

class RainfallViewModel(
    private val rainfallRepository: RainfallRepository,
    private val userRepository: UserRepository,
    private val waterUsageRepository: WaterUsageRepository
) : ViewModel() {

    val allRainfallData: StateFlow<List<RainfallEntity>> = rainfallRepository.allRainfallData
        .stateInWhileSubscribed(viewModelScope, emptyList())

    val userProfile: StateFlow<UserEntity?> = userRepository.user
        .stateInWhileSubscribed(viewModelScope, null)

    val totalWaterSaved: StateFlow<Double?> = rainfallRepository.totalWaterSaved
        .stateInWhileSubscribed(viewModelScope, 0.0)

    val totalWaterUsed: StateFlow<Double?> = waterUsageRepository.totalUsage
        .stateInWhileSubscribed(viewModelScope, 0.0)

    val availableWater: StateFlow<Double> = combine(
        totalWaterSaved,
        totalWaterUsed
    ) { saved, used ->
        (saved ?: 0.0) - (used ?: 0.0)
    }.stateInWhileSubscribed(viewModelScope, 0.0)

    val chartData: StateFlow<LineData?> = rainfallRepository.allRainfallData
        .map { history ->
            if (history.isEmpty()) null
            else {
                val entries = history.reversed().mapIndexed { index, entity ->
                    Entry(index.toFloat(), entity.rainfallMm.toFloat())
                }
                val dataSet = LineDataSet(entries, "Rainfall (mm)").apply {
                    color = Color.BLUE
                    setCircleColor(Color.BLUE)
                    setDrawValues(false)
                    lineWidth = 2f
                    circleRadius = 4f
                }
                LineData(dataSet)
            }
        }
        .stateInWhileSubscribed(viewModelScope, null)

    fun addRainfallData(rainfallMm: Double) {
        viewModelScope.launch {
            val profile = userProfile.value ?: return@launch
            val areaSqM = profile.roofArea * 0.0929
            val waterSaved = areaSqM * rainfallMm * 0.8
            
            val entity = RainfallEntity(
                date = System.currentTimeMillis(),
                rainfallMm = rainfallMm,
                waterSavedLiters = waterSaved
            )
            rainfallRepository.insert(entity)
        }
    }

    fun addWaterUsage(amountLiters: Double, category: String) {
        viewModelScope.launch {
            val entity = WaterUsageEntity(
                date = System.currentTimeMillis(),
                amountLiters = amountLiters,
                category = category
            )
            waterUsageRepository.insert(entity)
        }
    }

    fun saveUserProfile(name: String, roofArea: Double, tankCapacity: Double, location: String) {
        viewModelScope.launch {
            val user = UserEntity(
                name = name,
                roofArea = roofArea,
                tankCapacity = tankCapacity,
                location = location
            )
            userRepository.insert(user)
        }
    }
}
