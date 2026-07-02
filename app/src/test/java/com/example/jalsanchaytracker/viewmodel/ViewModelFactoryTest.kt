package com.example.jalsanchaytracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.jalsanchaytracker.repository.RainfallRepository
import com.example.jalsanchaytracker.repository.UserRepository
import com.example.jalsanchaytracker.repository.WaterUsageRepository
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ViewModelFactoryTest {

    private lateinit var rainfallRepository: RainfallRepository
    private lateinit var userRepository: UserRepository
    private lateinit var waterUsageRepository: WaterUsageRepository
    private lateinit var factory: ViewModelFactory

    @Before
    fun setup() {
        rainfallRepository = mock()
        userRepository = mock()
        waterUsageRepository = mock()

        whenever(rainfallRepository.allRainfallData).thenReturn(flowOf(emptyList()))
        whenever(rainfallRepository.totalWaterSaved).thenReturn(flowOf(0.0))
        whenever(userRepository.user).thenReturn(flowOf(null))
        whenever(waterUsageRepository.totalUsage).thenReturn(flowOf(0.0))
        whenever(waterUsageRepository.allUsage).thenReturn(flowOf(emptyList()))

        factory = ViewModelFactory(rainfallRepository, userRepository, waterUsageRepository)
    }

    @Test
    fun create_returnsRainfallViewModel() {
        val viewModel = factory.create(RainfallViewModel::class.java)
        assertNotNull(viewModel)
        assertTrue(viewModel is RainfallViewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun create_throwsForUnknownViewModel() {
        factory.create(UnknownViewModel::class.java)
    }

    private class UnknownViewModel : ViewModel()
}
