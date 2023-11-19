package com.example.domain.usecase

import com.example.domain.entities.Habit
import com.example.domain.entities.MessageDoHabit
import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import com.example.domain.repository.HabitListRepository
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat

/**
 * Unit tests for the implementation of [DoHabitUseCase].
 */
class DoHabitUseCaseTest {
    private lateinit var doHabitUseCase: DoHabitUseCase

    @Before
    fun setup() {
        val habitListRepository = mockk<HabitListRepository>(relaxed = true)
        doHabitUseCase = DoHabitUseCase(habitListRepository)
    }

    @Test
    fun `good habit, doneCount = 0`() = runTest {
        val habit = createHabit(type = Type.Good, doneCount = 0, number = 20)
        val result = doHabitUseCase.invoke(habit)
        assertThat(result.first, equalTo(MessageDoHabit.DoGoodHabitMore))
        assertThat(result.second, equalTo(19))
    }

    @Test
    fun `good habit, doneCount = number-1`() = runTest {
        val habit = createHabit(type = Type.Good, doneCount = 19, number = 20)
        val result = doHabitUseCase.invoke(habit)
        assertThat(result.first, equalTo(MessageDoHabit.DoGoodHabitEnough))
        assertThat(result.second, equalTo(0))
    }

    @Test
    fun `good habit, doneCount = number+1`() = runTest {
        val habit = createHabit(type = Type.Good, doneCount = 21, number = 20)
        val result = doHabitUseCase.invoke(habit)
        assertThat(result.first, equalTo(MessageDoHabit.DoGoodHabitEnough))
        assertThat(result.second, equalTo(-2))
    }

    @Test
    fun `good habit, doneCount equal number`() = runTest {
        val habit = createHabit(type = Type.Good, doneCount = 20, number = 20)
        val result = doHabitUseCase.invoke(habit)
        assertThat(result.first, equalTo(MessageDoHabit.DoGoodHabitEnough))
        assertThat(result.second, equalTo(-1))
    }

    @Test
    fun `good habit, doneCount bigger then number`() = runTest {
        val habit = createHabit(type = Type.Good, doneCount = 30, number = 20)
        val result = doHabitUseCase.invoke(habit)
        assertThat(result.first, equalTo(MessageDoHabit.DoGoodHabitEnough))
        assertThat(result.second, equalTo(-11))
    }

    @Test
    fun `bad habit, doneCount = 0`() = runTest {
        val habit = createHabit(type = Type.Bad, doneCount = 0, number = 20)
        val result = doHabitUseCase.invoke(habit)
        assertThat(result.first, equalTo(MessageDoHabit.DoBadHabitMore))
        assertThat(result.second, equalTo(19))
    }

    @Test
    fun `bad habit, doneCount = number-1`() = runTest {
        val habit = createHabit(type = Type.Bad, doneCount = 19, number = 20)
        val result = doHabitUseCase.invoke(habit)
        assertThat(result.first, equalTo(MessageDoHabit.DoBadHabitEnough))
        assertThat(result.second, equalTo(0))
    }

    @Test
    fun `bad habit, doneCount = number+1`() = runTest {
        val habit = createHabit(type = Type.Bad, doneCount = 21, number = 20)
        val result = doHabitUseCase.invoke(habit)
        assertThat(result.first, equalTo(MessageDoHabit.DoBadHabitEnough))
        assertThat(result.second, equalTo(-2))
    }

    @Test
    fun `bad habit, doneCount equal number`() = runTest {
        val habit = createHabit(type = Type.Bad, doneCount = 20, number = 20)
        val result = doHabitUseCase.invoke(habit)
        assertThat(result.first, equalTo(MessageDoHabit.DoBadHabitEnough))
        assertThat(result.second, equalTo(-1))
    }

    @Test
    fun `bad habit, doneCount bigger then number`() = runTest {
        val habit = createHabit(type = Type.Bad, doneCount = 30, number = 20)
        val result = doHabitUseCase.invoke(habit)
        assertThat(result.first, equalTo(MessageDoHabit.DoBadHabitEnough))
        assertThat(result.second, equalTo(-11))
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
            isSentToServer: Boolean = true,
            date: Int = 11,
            doneCount: Int = 0,
            id: String = "111",
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