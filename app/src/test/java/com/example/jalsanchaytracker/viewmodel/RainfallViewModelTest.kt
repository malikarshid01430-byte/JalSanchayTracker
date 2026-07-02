package com.example.jalsanchaytracker.viewmodel

import com.example.jalsanchaytracker.data.RainfallEntity
import com.example.jalsanchaytracker.data.UserEntity
import com.example.jalsanchaytracker.data.WaterUsageEntity
import com.example.jalsanchaytracker.repository.RainfallRepository
import com.example.jalsanchaytracker.repository.UserRepository
import com.example.jalsanchaytracker.repository.WaterUsageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class RainfallViewModelTest {

    private lateinit var rainfallRepository: RainfallRepository
    private lateinit var userRepository: UserRepository
    private lateinit var waterUsageRepository: WaterUsageRepository
    private lateinit var viewModel: RainfallViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    private val rainfallDataFlow = MutableStateFlow<List<RainfallEntity>>(emptyList())
    private val totalWaterSavedFlow = MutableStateFlow<Double?>(0.0)
    private val userFlow = MutableStateFlow<UserEntity?>(null)
    private val totalUsageFlow = MutableStateFlow<Double?>(0.0)
    private val allUsageFlow = MutableStateFlow<List<WaterUsageEntity>>(emptyList())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        rainfallRepository = mock()
        userRepository = mock()
        waterUsageRepository = mock()

        whenever(rainfallRepository.allRainfallData).thenReturn(rainfallDataFlow)
        whenever(rainfallRepository.totalWaterSaved).thenReturn(totalWaterSavedFlow)
        whenever(userRepository.user).thenReturn(userFlow)
        whenever(waterUsageRepository.totalUsage).thenReturn(totalUsageFlow)
        whenever(waterUsageRepository.allUsage).thenReturn(allUsageFlow)

        viewModel = RainfallViewModel(rainfallRepository, userRepository, waterUsageRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun allRainfallData_emptyInitially() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.allRainfallData.collect {}
        }
        assertEquals(emptyList<RainfallEntity>(), viewModel.allRainfallData.value)
        job.cancel()
    }

    @Test
    fun allRainfallData_reflectsRepository() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.allRainfallData.collect {}
        }
        val data = listOf(
            RainfallEntity(id = 1, date = 100L, rainfallMm = 10.0, waterSavedLiters = 80.0),
            RainfallEntity(id = 2, date = 200L, rainfallMm = 20.0, waterSavedLiters = 160.0)
        )
        rainfallDataFlow.value = data
        assertEquals(2, viewModel.allRainfallData.value.size)
        job.cancel()
    }

    @Test
    fun userProfile_nullWhenNoUser() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.userProfile.collect {}
        }
        assertNull(viewModel.userProfile.value)
        job.cancel()
    }

    @Test
    fun userProfile_reflectsRepository() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.userProfile.collect {}
        }
        val user = UserEntity(name = "Arshid", roofArea = 200.0, tankCapacity = 5000.0, location = "Srinagar")
        userFlow.value = user
        assertEquals("Arshid", viewModel.userProfile.value?.name)
        job.cancel()
    }

    @Test
    fun totalWaterSaved_reflectsRepository() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.totalWaterSaved.collect {}
        }
        totalWaterSavedFlow.value = 500.0
        assertEquals(500.0, viewModel.totalWaterSaved.value ?: 0.0, 0.001)
        job.cancel()
    }

    @Test
    fun totalWaterUsed_reflectsRepository() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.totalWaterUsed.collect {}
        }
        totalUsageFlow.value = 200.0
        assertEquals(200.0, viewModel.totalWaterUsed.value ?: 0.0, 0.001)
        job.cancel()
    }

    @Test
    fun availableWater_calculatesCorrectly() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.availableWater.collect {}
        }
        totalWaterSavedFlow.value = 500.0
        totalUsageFlow.value = 200.0
        assertEquals(300.0, viewModel.availableWater.value, 0.001)
        job.cancel()
    }

    @Test
    fun availableWater_handlesNullSaved() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.availableWater.collect {}
        }
        totalWaterSavedFlow.value = null
        totalUsageFlow.value = 100.0
        assertEquals(-100.0, viewModel.availableWater.value, 0.001)
        job.cancel()
    }

    @Test
    fun availableWater_handlesNullUsed() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.availableWater.collect {}
        }
        totalWaterSavedFlow.value = 300.0
        totalUsageFlow.value = null
        assertEquals(300.0, viewModel.availableWater.value, 0.001)
        job.cancel()
    }

    @Test
    fun availableWater_handlesBothNull() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.availableWater.collect {}
        }
        totalWaterSavedFlow.value = null
        totalUsageFlow.value = null
        assertEquals(0.0, viewModel.availableWater.value, 0.001)
        job.cancel()
    }

    @Test
    fun availableWater_canBeNegative() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.availableWater.collect {}
        }
        totalWaterSavedFlow.value = 100.0
        totalUsageFlow.value = 250.0
        assertEquals(-150.0, viewModel.availableWater.value, 0.001)
        job.cancel()
    }

    @Test
    fun addRainfallData_calculatesWaterSaved() = runTest {
        val profileJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.userProfile.collect {}
        }
        val user = UserEntity(name = "Test", roofArea = 100.0, tankCapacity = 5000.0, location = "Delhi")
        userFlow.value = user
        advanceUntilIdle()

        viewModel.addRainfallData(10.0)
        advanceUntilIdle()

        // roofArea=100 sqft -> areaSqM = 100 * 0.0929 = 9.29
        // waterSaved = 9.29 * 10.0 * 0.8 = 74.32
        val captor = argumentCaptor<RainfallEntity>()
        verify(rainfallRepository).insert(captor.capture())
        val inserted = captor.firstValue
        assertEquals(10.0, inserted.rainfallMm, 0.001)
        assertEquals(74.32, inserted.waterSavedLiters, 0.01)
        profileJob.cancel()
    }

    @Test
    fun addRainfallData_doesNothingWithoutProfile() = runTest {
        val profileJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.userProfile.collect {}
        }
        advanceUntilIdle()

        viewModel.addRainfallData(10.0)
        advanceUntilIdle()

        verify(rainfallRepository, never()).insert(any())
        profileJob.cancel()
    }

    @Test
    fun addRainfallData_waterSavedScalesWithRoofArea() = runTest {
        val profileJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.userProfile.collect {}
        }
        val largeRoof = UserEntity(name = "Big", roofArea = 500.0, tankCapacity = 10000.0, location = "Mumbai")
        userFlow.value = largeRoof
        advanceUntilIdle()

        viewModel.addRainfallData(20.0)
        advanceUntilIdle()

        // areaSqM = 500 * 0.0929 = 46.45
        // waterSaved = 46.45 * 20 * 0.8 = 743.2
        val captor = argumentCaptor<RainfallEntity>()
        verify(rainfallRepository).insert(captor.capture())
        assertEquals(743.2, captor.firstValue.waterSavedLiters, 0.01)
        profileJob.cancel()
    }

    @Test
    fun addWaterUsage_insertsCorrectEntity() = runTest {
        viewModel.addWaterUsage(50.0, "Bathing")
        advanceUntilIdle()

        val captor = argumentCaptor<WaterUsageEntity>()
        verify(waterUsageRepository).insert(captor.capture())
        val inserted = captor.firstValue
        assertEquals(50.0, inserted.amountLiters, 0.001)
        assertEquals("Bathing", inserted.category)
        assertTrue(inserted.date > 0)
    }

    @Test
    fun addWaterUsage_differentCategories() = runTest {
        viewModel.addWaterUsage(10.0, "Drinking")
        viewModel.addWaterUsage(30.0, "Cooking")
        advanceUntilIdle()

        verify(waterUsageRepository, times(2)).insert(any())
    }

    @Test
    fun saveUserProfile_insertsCorrectEntity() = runTest {
        viewModel.saveUserProfile("Arshid", 200.0, 5000.0, "Srinagar")
        advanceUntilIdle()

        val captor = argumentCaptor<UserEntity>()
        verify(userRepository).insert(captor.capture())
        val inserted = captor.firstValue
        assertEquals("Arshid", inserted.name)
        assertEquals(200.0, inserted.roofArea, 0.001)
        assertEquals(5000.0, inserted.tankCapacity, 0.001)
        assertEquals("Srinagar", inserted.location)
    }

    @Test
    fun chartData_nullWhenNoData() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.chartData.collect {}
        }
        assertNull(viewModel.chartData.value)
        job.cancel()
    }
}
