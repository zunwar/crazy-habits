package com.example.data

import com.example.data.local.database.HabitDao
import com.example.data.mappers.toEntity
import com.example.data.remote.network.HabitUID
import com.example.data.remote.network.HabitsApiService
import com.example.data.remote.network.NetworkResult
import com.example.data.remote.network.ResponseError
import com.example.domain.entities.Habit
import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the implementation of [HabitEditRepositoryImpl].
 */
class HabitEditRepositoryImplTest {

    private lateinit var habitEditRepositoryImpl: HabitEditRepositoryImpl
    private lateinit var retrofitService: HabitsApiService
    private lateinit var habitDao: HabitDao

    @Before
    fun setup() {
        habitDao = mockk(relaxed = true)
        retrofitService = mockk()
        habitEditRepositoryImpl = HabitEditRepositoryImpl(habitDao, retrofitService)
    }

    @Test
    fun `addHabit - success network call, id from server saved to DB`() = runTest {
        coEvery { retrofitService.putHabit(habitDto = any()) } returns NetworkResult.Success(
            HabitUID("111"),
            200,
            "success"
        )
        val result = habitEditRepositoryImpl.addHabit(testHabit)
        coVerify { retrofitService.putHabit(habitDto = any()) }
        coVerify {
            habitDao.insertAll(testHabit.copy(isSentToServer = true, id = "111").toEntity())
        }
        assertThat(result, equalTo("200 success"))
    }

    @Test
    fun `addHabit - error network call`() = runTest {
        coEvery { retrofitService.putHabit(habitDto = any()) } returns NetworkResult.Error(
            400,
            "error",
            responseError = ResponseError(400, "error")
        )
        val result = habitEditRepositoryImpl.addHabit(testHabit)
        coVerify { retrofitService.putHabit(habitDto = any()) }
        coVerify { habitDao.insertAll(any()) }
        assertThat(result, containsString("400"))
    }

    @Test
    fun `addHabit - exception network call`() = runTest {
        coEvery { retrofitService.putHabit(habitDto = any()) } returns NetworkResult.Exception(
            Throwable(message = "exception")
        )
        val result = habitEditRepositoryImpl.addHabit(testHabit)
        coVerify { retrofitService.putHabit(habitDto = any()) }
        coVerify { habitDao.insertAll(any()) }
        assertThat(result, equalTo("exception"))
    }

    @Test
    fun `changeHabit - success network call `() = runTest {
        coEvery { retrofitService.changeHabit(habitDto = any()) } returns NetworkResult.Success(
            HabitUID("111"),
            200,
            "success"
        )
        val result = habitEditRepositoryImpl.changeHabit(testHabit)
        coVerify { retrofitService.changeHabit(habitDto = any()) }
        coVerify { habitDao.updateHabit(testHabit.copy(isSentToServer = true).toEntity()) }
        assertThat(result, equalTo("200 success"))
    }

    @Test
    fun `changeHabit - error network call `() = runTest {
        coEvery { retrofitService.changeHabit(habitDto = any()) } returns NetworkResult.Error(
            400,
            "error",
            responseError = ResponseError(400, "error")
        )
        val result = habitEditRepositoryImpl.changeHabit(testHabit)
        coVerify { habitDao.updateHabit(testHabit.copy(isSentToServer = false).toEntity()) }
        assertThat(result, containsString("400"))
    }

    @Test
    fun `changeHabit - exception network call `() = runTest {
        coEvery { retrofitService.changeHabit(habitDto = any()) } returns NetworkResult.Exception(
            Throwable(message = "exception")
        )
        val result = habitEditRepositoryImpl.changeHabit(testHabit)
        coVerify { habitDao.updateHabit(testHabit.copy(isSentToServer = false).toEntity()) }
        assertThat(result, equalTo("exception"))
    }

    @Test
    fun `getHabitToEdit - returns right habit`() = runTest {
        coEvery { habitDao.getHabitById(any()) } returns flowOf(testHabit.toEntity())
        val result = habitEditRepositoryImpl.getHabitToEdit(testHabit.id).first()
        coVerify { habitDao.getHabitById(testHabit.id) }
        assertThat(result, equalTo(testHabit))
    }

    companion object {
        val testHabit = Habit(
            name = "aa",
            desc = "bb",
            type = Type.Good,
            priority = Priority.High,
            number = 2,
            frequency = 3,
            colorHabit = -1,
            isSentToServer = false,
            date = 11,
            doneCount = 0,
            id = "uid",
        )
    }

}