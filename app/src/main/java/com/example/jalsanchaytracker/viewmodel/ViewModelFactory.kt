package com.example.jalsanchaytracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jalsanchaytracker.repository.RainfallRepository
import com.example.jalsanchaytracker.repository.UserRepository
import com.example.jalsanchaytracker.repository.WaterUsageRepository

class ViewModelFactory(
    private val rainfallRepository: RainfallRepository,
    private val userRepository: UserRepository,
    private val waterUsageRepository: WaterUsageRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RainfallViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RainfallViewModel(rainfallRepository, userRepository, waterUsageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
