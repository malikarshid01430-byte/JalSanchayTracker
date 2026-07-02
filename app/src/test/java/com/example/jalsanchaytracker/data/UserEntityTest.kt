package com.example.jalsanchaytracker.data

import org.junit.Assert.*
import org.junit.Test

class UserEntityTest {

    @Test
    fun defaultValues_areCorrect() {
        val user = UserEntity()
        assertEquals(1, user.id)
        assertEquals("", user.name)
        assertEquals(0.0, user.roofArea, 0.001)
        assertEquals(0.0, user.tankCapacity, 0.001)
        assertEquals("", user.location)
    }

    @Test
    fun customValues_areRetained() {
        val user = UserEntity(
            id = 5,
            name = "Arshid",
            roofArea = 150.5,
            tankCapacity = 5000.0,
            location = "Srinagar"
        )
        assertEquals(5, user.id)
        assertEquals("Arshid", user.name)
        assertEquals(150.5, user.roofArea, 0.001)
        assertEquals(5000.0, user.tankCapacity, 0.001)
        assertEquals("Srinagar", user.location)
    }

    @Test
    fun copy_createsModifiedInstance() {
        val original = UserEntity(name = "Alice", roofArea = 100.0, tankCapacity = 3000.0, location = "Delhi")
        val copy = original.copy(name = "Bob", location = "Mumbai")
        assertEquals("Bob", copy.name)
        assertEquals("Mumbai", copy.location)
        assertEquals(100.0, copy.roofArea, 0.001)
        assertEquals(3000.0, copy.tankCapacity, 0.001)
    }

    @Test
    fun equality_worksForDataClass() {
        val user1 = UserEntity(id = 1, name = "Test", roofArea = 50.0, tankCapacity = 1000.0, location = "Jammu")
        val user2 = UserEntity(id = 1, name = "Test", roofArea = 50.0, tankCapacity = 1000.0, location = "Jammu")
        assertEquals(user1, user2)
        assertEquals(user1.hashCode(), user2.hashCode())
    }

    @Test
    fun inequality_whenFieldsDiffer() {
        val user1 = UserEntity(name = "Alice")
        val user2 = UserEntity(name = "Bob")
        assertNotEquals(user1, user2)
    }
}
