package com.example.jalsanchaytracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jalsanchaytracker.data.RainfallEntity
import com.example.jalsanchaytracker.data.UserEntity
import com.example.jalsanchaytracker.data.WaterUsageEntity
import com.example.jalsanchaytracker.repository.RainfallRepository
import com.example.jalsanchaytracker.repository.UserRepository
import com.example.jalsanchaytracker.repository.WaterUsageRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.graphics.Color

sealed interface UiEvent {
    data class ShowError(val message: String) : UiEvent
    data class ShowSuccess(val message: String) : UiEvent
}

class RainfallViewModel(
    private val rainfallRepository: RainfallRepository,
    private val userRepository: UserRepository,
    private val waterUsageRepository: WaterUsageRepository
) : ViewModel() {

    private val _uiEvents = Channel<UiEvent>(Channel.BUFFERED)
    val uiEvents: Flow<UiEvent> = _uiEvents.receiveAsFlow()

    val allRainfallData: StateFlow<List<RainfallEntity>> = rainfallRepository.allRainfallData
        .catch { e ->
            Log.e("RainfallViewModel", "Error loading rainfall data", e)
            _uiEvents.send(UiEvent.ShowError("Failed to load rainfall data"))
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserEntity?> = userRepository.user
        .catch { e ->
            Log.e("RainfallViewModel", "Error loading user profile", e)
            _uiEvents.send(UiEvent.ShowError("Failed to load user profile"))
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val totalWaterSaved: StateFlow<Double?> = rainfallRepository.totalWaterSaved
        .catch { e ->
            Log.e("RainfallViewModel", "Error loading total water saved", e)
            _uiEvents.send(UiEvent.ShowError("Failed to load water savings data"))
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalWaterUsed: StateFlow<Double?> = waterUsageRepository.totalUsage
        .catch { e ->
            Log.e("RainfallViewModel", "Error loading total water used", e)
            _uiEvents.send(UiEvent.ShowError("Failed to load water usage data"))
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val availableWater: StateFlow<Double> = combine(
        totalWaterSaved,
        totalWaterUsed
    ) { saved, used ->
        (saved ?: 0.0) - (used ?: 0.0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

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
        .catch { e ->
            Log.e("RainfallViewModel", "Error generating chart data", e)
            _uiEvents.send(UiEvent.ShowError("Failed to generate chart"))
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun addRainfallData(rainfallMm: Double) {
        viewModelScope.launch {
            try {
                val profile = userProfile.value
                if (profile == null) {
                    _uiEvents.send(UiEvent.ShowError("Please set up your profile before adding rainfall data"))
                    return@launch
                }
                val areaSqM = profile.roofArea * 0.0929
                val waterSaved = areaSqM * rainfallMm * 0.8

                val entity = RainfallEntity(
                    date = System.currentTimeMillis(),
                    rainfallMm = rainfallMm,
                    waterSavedLiters = waterSaved
                )
                rainfallRepository.insert(entity)
                _uiEvents.send(UiEvent.ShowSuccess("Rainfall data saved successfully"))
            } catch (e: Exception) {
                Log.e("RainfallViewModel", "Error saving rainfall data", e)
                _uiEvents.send(UiEvent.ShowError("Failed to save rainfall data: ${e.localizedMessage}"))
            }
        }
    }

    fun addWaterUsage(amountLiters: Double, category: String) {
        viewModelScope.launch {
            try {
                if (amountLiters <= 0) {
                    _uiEvents.send(UiEvent.ShowError("Water usage amount must be greater than zero"))
                    return@launch
                }
                val entity = WaterUsageEntity(
                    date = System.currentTimeMillis(),
                    amountLiters = amountLiters,
                    category = category
                )
                waterUsageRepository.insert(entity)
                _uiEvents.send(UiEvent.ShowSuccess("Water usage logged successfully"))
            } catch (e: Exception) {
                Log.e("RainfallViewModel", "Error saving water usage", e)
                _uiEvents.send(UiEvent.ShowError("Failed to log water usage: ${e.localizedMessage}"))
            }
        }
    }

    fun saveUserProfile(name: String, roofArea: Double, tankCapacity: Double, location: String) {
        viewModelScope.launch {
            try {
                if (name.isBlank()) {
                    _uiEvents.send(UiEvent.ShowError("Name cannot be empty"))
                    return@launch
                }
                if (roofArea <= 0) {
                    _uiEvents.send(UiEvent.ShowError("Roof area must be greater than zero"))
                    return@launch
                }
                if (tankCapacity <= 0) {
                    _uiEvents.send(UiEvent.ShowError("Tank capacity must be greater than zero"))
                    return@launch
                }
                val user = UserEntity(
                    name = name,
                    roofArea = roofArea,
                    tankCapacity = tankCapacity,
                    location = location
                )
                userRepository.insert(user)
                _uiEvents.send(UiEvent.ShowSuccess("Profile saved successfully"))
            } catch (e: Exception) {
                Log.e("RainfallViewModel", "Error saving user profile", e)
                _uiEvents.send(UiEvent.ShowError("Failed to save profile: ${e.localizedMessage}"))
            }
        }
    }
}
