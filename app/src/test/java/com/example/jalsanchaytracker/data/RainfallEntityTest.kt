package com.example.jalsanchaytracker.data

import org.junit.Assert.*
import org.junit.Test

class RainfallEntityTest {

    @Test
    fun creation_withRequiredFields() {
        val rainfall = RainfallEntity(
            date = 1700000000000L,
            rainfallMm = 15.0,
            waterSavedLiters = 120.0
        )
        assertEquals(0, rainfall.id)
        assertEquals(1700000000000L, rainfall.date)
        assertEquals(15.0, rainfall.rainfallMm, 0.001)
        assertEquals(120.0, rainfall.waterSavedLiters, 0.001)
    }

    @Test
    fun creation_withCustomId() {
        val rainfall = RainfallEntity(
            id = 7,
            date = 1700000000000L,
            rainfallMm = 30.0,
            waterSavedLiters = 250.0
        )
        assertEquals(7, rainfall.id)
    }

    @Test
    fun copy_modifiesSpecificFields() {
        val original = RainfallEntity(date = 100L, rainfallMm = 10.0, waterSavedLiters = 80.0)
        val modified = original.copy(rainfallMm = 25.0, waterSavedLiters = 200.0)
        assertEquals(25.0, modified.rainfallMm, 0.001)
        assertEquals(200.0, modified.waterSavedLiters, 0.001)
        assertEquals(original.date, modified.date)
    }

    @Test
    fun equality_forIdenticalEntities() {
        val r1 = RainfallEntity(id = 1, date = 100L, rainfallMm = 10.0, waterSavedLiters = 80.0)
        val r2 = RainfallEntity(id = 1, date = 100L, rainfallMm = 10.0, waterSavedLiters = 80.0)
        assertEquals(r1, r2)
        assertEquals(r1.hashCode(), r2.hashCode())
    }

    @Test
    fun inequality_whenRainfallDiffers() {
        val r1 = RainfallEntity(date = 100L, rainfallMm = 10.0, waterSavedLiters = 80.0)
        val r2 = RainfallEntity(date = 100L, rainfallMm = 20.0, waterSavedLiters = 80.0)
        assertNotEquals(r1, r2)
    }

    @Test
    fun waterSaved_canBeZero() {
        val rainfall = RainfallEntity(date = 100L, rainfallMm = 5.0, waterSavedLiters = 0.0)
        assertEquals(0.0, rainfall.waterSavedLiters, 0.001)
    }
}
