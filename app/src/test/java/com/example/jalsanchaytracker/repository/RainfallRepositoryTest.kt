package com.example.jalsanchaytracker.repository

import com.example.jalsanchaytracker.data.RainfallDao
import com.example.jalsanchaytracker.data.RainfallEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RainfallRepositoryTest {

    private lateinit var rainfallDao: RainfallDao
    private lateinit var repository: RainfallRepository

    @Before
    fun setup() {
        rainfallDao = mock()
        whenever(rainfallDao.getAllRainfallData()).thenReturn(flowOf(emptyList()))
        whenever(rainfallDao.getTotalWaterSaved()).thenReturn(flowOf(0.0))
        repository = RainfallRepository(rainfallDao)
    }

    @Test
    fun allRainfallData_returnsEmptyListInitially() = runTest {
        val result = repository.allRainfallData.first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun allRainfallData_returnsDaoData() = runTest {
        val data = listOf(
            RainfallEntity(id = 1, date = 100L, rainfallMm = 10.0, waterSavedLiters = 80.0),
            RainfallEntity(id = 2, date = 200L, rainfallMm = 20.0, waterSavedLiters = 160.0)
        )
        whenever(rainfallDao.getAllRainfallData()).thenReturn(flowOf(data))
        repository = RainfallRepository(rainfallDao)

        val result = repository.allRainfallData.first()
        assertEquals(2, result.size)
        assertEquals(10.0, result[0].rainfallMm, 0.001)
        assertEquals(20.0, result[1].rainfallMm, 0.001)
    }

    @Test
    fun totalWaterSaved_delegatesToDao() = runTest {
        whenever(rainfallDao.getTotalWaterSaved()).thenReturn(flowOf(500.0))
        repository = RainfallRepository(rainfallDao)

        val result = repository.totalWaterSaved.first()
        assertEquals(500.0, result ?: 0.0, 0.001)
    }

    @Test
    fun totalWaterSaved_returnsNullFromDao() = runTest {
        whenever(rainfallDao.getTotalWaterSaved()).thenReturn(flowOf(null))
        repository = RainfallRepository(rainfallDao)

        val result = repository.totalWaterSaved.first()
        assertNull(result)
    }

    @Test
    fun insert_delegatesToDao() = runTest {
        val rainfall = RainfallEntity(date = 100L, rainfallMm = 15.0, waterSavedLiters = 120.0)
        repository.insert(rainfall)
        verify(rainfallDao).insertRainfall(rainfall)
    }

    @Test
    fun delete_delegatesToDao() = runTest {
        val rainfall = RainfallEntity(id = 5, date = 100L, rainfallMm = 15.0, waterSavedLiters = 120.0)
        repository.delete(rainfall)
        verify(rainfallDao).deleteRainfall(rainfall)
    }
}
