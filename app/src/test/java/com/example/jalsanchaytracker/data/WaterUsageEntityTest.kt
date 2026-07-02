package com.example.jalsanchaytracker.data

import org.junit.Assert.*
import org.junit.Test

class WaterUsageEntityTest {

    @Test
    fun creation_withRequiredFields() {
        val usage = WaterUsageEntity(
            date = 1700000000000L,
            amountLiters = 25.5,
            category = "Drinking"
        )
        assertEquals(0, usage.id)
        assertEquals(1700000000000L, usage.date)
        assertEquals(25.5, usage.amountLiters, 0.001)
        assertEquals("Drinking", usage.category)
    }

    @Test
    fun creation_withCustomId() {
        val usage = WaterUsageEntity(
            id = 42,
            date = 1700000000000L,
            amountLiters = 100.0,
            category = "Irrigation"
        )
        assertEquals(42, usage.id)
    }

    @Test
    fun copy_modifiesSpecificFields() {
        val original = WaterUsageEntity(date = 1700000000000L, amountLiters = 10.0, category = "Cooking")
        val modified = original.copy(amountLiters = 20.0, category = "Bathing")
        assertEquals(20.0, modified.amountLiters, 0.001)
        assertEquals("Bathing", modified.category)
        assertEquals(original.date, modified.date)
    }

    @Test
    fun equality_forIdenticalEntities() {
        val usage1 = WaterUsageEntity(id = 1, date = 100L, amountLiters = 50.0, category = "Washing")
        val usage2 = WaterUsageEntity(id = 1, date = 100L, amountLiters = 50.0, category = "Washing")
        assertEquals(usage1, usage2)
        assertEquals(usage1.hashCode(), usage2.hashCode())
    }

    @Test
    fun inequality_whenFieldsDiffer() {
        val usage1 = WaterUsageEntity(date = 100L, amountLiters = 50.0, category = "Washing")
        val usage2 = WaterUsageEntity(date = 100L, amountLiters = 75.0, category = "Washing")
        assertNotEquals(usage1, usage2)
    }

    @Test
    fun differentCategories_createDistinctEntities() {
        val categories = listOf("Drinking", "Cooking", "Bathing", "Irrigation", "Washing")
        val entities = categories.map { cat ->
            WaterUsageEntity(date = 100L, amountLiters = 10.0, category = cat)
        }
        assertEquals(categories.size, entities.map { it.category }.distinct().size)
    }
}
