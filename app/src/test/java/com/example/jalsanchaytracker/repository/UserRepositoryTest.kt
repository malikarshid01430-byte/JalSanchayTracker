package com.example.jalsanchaytracker.repository

import com.example.jalsanchaytracker.data.UserDao
import com.example.jalsanchaytracker.data.UserEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UserRepositoryTest {

    private lateinit var userDao: UserDao
    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        userDao = mock()
    }

    @Test
    fun user_delegatesToDao() = runTest {
        val userEntity = UserEntity(name = "Arshid", roofArea = 100.0, tankCapacity = 5000.0, location = "Srinagar")
        whenever(userDao.getUser()).thenReturn(flowOf(userEntity))
        repository = UserRepository(userDao)

        val result = repository.user.first()
        assertNotNull(result)
        assertEquals("Arshid", result?.name)
        assertEquals(100.0, result?.roofArea ?: 0.0, 0.001)
    }

    @Test
    fun user_returnsNullWhenNoUser() = runTest {
        whenever(userDao.getUser()).thenReturn(flowOf(null))
        repository = UserRepository(userDao)

        val result = repository.user.first()
        assertNull(result)
    }

    @Test
    fun insert_delegatesToDao() = runTest {
        whenever(userDao.getUser()).thenReturn(flowOf(null))
        repository = UserRepository(userDao)

        val user = UserEntity(name = "Test", roofArea = 50.0, tankCapacity = 1000.0, location = "Delhi")
        repository.insert(user)
        verify(userDao).insertUser(user)
    }
}
