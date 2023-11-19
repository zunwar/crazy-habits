package com.example.data

import com.example.data.entities.HabitDto
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
    fun setUp() {
        habitDao = mockk(relaxed = true)
        retrofitService = mockk()
        val habitDtoSlot = slot<HabitDto>()
        coEvery { retrofitService.putHabit(habitDto = capture(habitDtoSlot)) } answers {
            when (habitDtoSlot.captured.name) {
                "success" -> NetworkResult.Success(
                    HabitUID("111"),
                    200,
                    "success"
                )
                "error" -> NetworkResult.Error(
                    400,
                    "error",
                    responseError = ResponseError(400, "error")
                )
                else -> NetworkResult.Exception(Throwable(message = "exception"))
            }
        }
        coEvery { retrofitService.changeHabit(habitDto = capture(habitDtoSlot)) } answers {
            when (habitDtoSlot.captured.name) {
                "success" -> NetworkResult.Success(
                    HabitUID("111"),
                    200,
                    "success"
                )
                "error" -> NetworkResult.Error(
                    400,
                    "error",
                    responseError = ResponseError(400, "error")
                )
                else -> NetworkResult.Exception(Throwable(message = "exception"))
            }
        }
        habitEditRepositoryImpl = HabitEditRepositoryImpl(habitDao, retrofitService)
    }

    @Test
    fun `addHabit - success network call, id from server saved to DB`() = runTest {
        val habitToSave: Habit = createHabit(name = "success")
        val result = habitEditRepositoryImpl.addHabit(habitToSave)
        coVerify { retrofitService.putHabit(habitDto = any()) }
        coVerify { habitDao.insertAll(any()) }
        coVerify {
            habitDao.insertAll(
                habitToSave.copy(isSentToServer = true, id = "111").toEntity()
            )
        }
        assertThat(result, equalTo("200 success"))
    }

    @Test
    fun `addHabit - error network call`() = runTest {
        val result = habitEditRepositoryImpl.addHabit(createHabit(name = "error"))
        coVerify { retrofitService.putHabit(habitDto = any()) }
        coVerify { habitDao.insertAll(any()) }
        assertThat(result, containsString("400"))
    }

    @Test
    fun `addHabit - exception network call`() = runTest {
        val result = habitEditRepositoryImpl.addHabit(createHabit(name = "exception"))
        coVerify { retrofitService.putHabit(habitDto = any()) }
        coVerify { habitDao.insertAll(any()) }
        assertThat(result, equalTo("exception"))
    }

    @Test
    fun `changeHabit - success network call `() = runTest {
        val habitToChange: Habit = createHabit(name = "success")
        val result = habitEditRepositoryImpl.changeHabit(habitToChange)
        coVerify { retrofitService.changeHabit(habitDto = any()) }
        coVerify { habitDao.updateHabit(habitToChange.copy(isSentToServer = true).toEntity()) }
        assertThat(result, equalTo("200 success"))
    }

    @Test
    fun `changeHabit - error network call `() = runTest {
        val habitToChange: Habit = createHabit(name = "error")
        val result = habitEditRepositoryImpl.changeHabit(habitToChange)
        coVerify { habitDao.updateHabit(habitToChange.copy(isSentToServer = false).toEntity()) }
        assertThat(result, containsString("400"))
    }

    @Test
    fun `changeHabit - exception network call `() = runTest {
        val habitToChange: Habit = createHabit(name = "exception")
        val result = habitEditRepositoryImpl.changeHabit(habitToChange)
        coVerify { habitDao.updateHabit(habitToChange.copy(isSentToServer = false).toEntity()) }
        assertThat(result, equalTo("exception"))
    }

    @Test
    fun `getHabitToEdit - returns right habit`() = runTest {
        val idHabit = "222"
        coEvery { habitDao.getHabitById(any()) } returns flowOf(createHabit(id = idHabit).toEntity())
        val result = habitEditRepositoryImpl.getHabitToEdit(idHabit).first()
        coVerify { habitDao.getHabitById(idHabit) }
        assertThat(result, equalTo(createHabit(id = idHabit)))
    }

    companion object {
        fun createHabit(
            name: String = "aa",
            desc: String = "bb",
            type: Type = Type.Good,
            priority: Priority = Priority.High,
            number: Int = 2,
            frequency: Int = 3,
            colorHabit: Int = -1,
            isSentToServer: Boolean = false,
            date: Int = 11,
            doneCount: Int = 0,
            id: String = "uid",
        ) = Habit(
            name = name,
            desc = desc,
            type = type,
            priority = priority,
            number = number,
            frequency = frequency,
            colorHabit = colorHabit,
            isSentToServer = isSentToServer,
            date = date,
            doneCount = doneCount,
            id = id,
        )
    }
}