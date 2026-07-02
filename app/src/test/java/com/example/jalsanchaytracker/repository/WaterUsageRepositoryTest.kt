package com.example.jalsanchaytracker.repository

import com.example.jalsanchaytracker.data.WaterUsageDao
import com.example.jalsanchaytracker.data.WaterUsageEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class WaterUsageRepositoryTest {

    private lateinit var waterUsageDao: WaterUsageDao
    private lateinit var repository: WaterUsageRepository

    @Before
    fun setup() {
        waterUsageDao = mock()
        whenever(waterUsageDao.getAllUsage()).thenReturn(flowOf(emptyList()))
        whenever(waterUsageDao.getTotalUsage()).thenReturn(flowOf(0.0))
        repository = WaterUsageRepository(waterUsageDao)
    }

    @Test
    fun allUsage_returnsEmptyListInitially() = runTest {
        val result = repository.allUsage.first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun allUsage_returnsDaoData() = runTest {
        val data = listOf(
            WaterUsageEntity(id = 1, date = 100L, amountLiters = 25.0, category = "Drinking"),
            WaterUsageEntity(id = 2, date = 200L, amountLiters = 50.0, category = "Cooking"),
            WaterUsageEntity(id = 3, date = 300L, amountLiters = 100.0, category = "Bathing")
        )
        whenever(waterUsageDao.getAllUsage()).thenReturn(flowOf(data))
        repository = WaterUsageRepository(waterUsageDao)

        val result = repository.allUsage.first()
        assertEquals(3, result.size)
        assertEquals("Drinking", result[0].category)
        assertEquals("Cooking", result[1].category)
        assertEquals("Bathing", result[2].category)
    }

    @Test
    fun totalUsage_delegatesToDao() = runTest {
        whenever(waterUsageDao.getTotalUsage()).thenReturn(flowOf(175.0))
        repository = WaterUsageRepository(waterUsageDao)

        val result = repository.totalUsage.first()
        assertEquals(175.0, result ?: 0.0, 0.001)
    }

    @Test
    fun totalUsage_returnsNullFromDao() = runTest {
        whenever(waterUsageDao.getTotalUsage()).thenReturn(flowOf(null))
        repository = WaterUsageRepository(waterUsageDao)

        val result = repository.totalUsage.first()
        assertNull(result)
    }

    @Test
    fun insert_delegatesToDao() = runTest {
        val usage = WaterUsageEntity(date = 100L, amountLiters = 30.0, category = "Irrigation")
        repository.insert(usage)
        verify(waterUsageDao).insertUsage(usage)
    }
}
