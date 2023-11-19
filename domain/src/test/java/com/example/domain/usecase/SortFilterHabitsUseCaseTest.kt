package com.example.domain.usecase

import com.example.domain.entities.*
import com.example.domain.repository.HabitListRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the implementation of [SortFilterHabitsUseCase].
 */
class SortFilterHabitsUseCaseTest {

    private lateinit var sortFilterHabitsUseCase: SortFilterHabitsUseCase

    @Before
    fun setUp() {
        val habitListRepository = mockk<HabitListRepository>()
        coEvery {
            habitListRepository.getHabitsByNameAndTypeAndSortASC(any(), any())
        } returns listASC()
        coEvery {
            habitListRepository.getHabitsByNameAndTypeAndSortDESC(any(), any())
        } returns listDesc()
        coEvery { habitListRepository.getHabitsByNameAndType(any(), any()) } returns noSortList()

        sortFilterHabitsUseCase = SortFilterHabitsUseCase(habitListRepository)
    }

    @Test
    fun `sort ASC`() = runTest {
        val sortState = SortState.SortASC
        val nameToFilter = NameToFilter("")
        val sortOrFilter = Pair(sortState, nameToFilter)
        val type = Type.Good

        val result = sortFilterHabitsUseCase(sortOrFilter, type).first()
        assertThat(result, equalTo(listASC().first()))
    }

    @Test
    fun `sort Desc`() = runTest {
        val sortState = SortState.SortDESC
        val nameToFilter = NameToFilter("")
        val sortOrFilter = Pair(sortState, nameToFilter)
        val type = Type.Good

        val result = sortFilterHabitsUseCase(sortOrFilter, type).first()
        assertThat(result, equalTo(listDesc().first()))
    }

    @Test
    fun `no Sort`() = runTest {
        val sortState = SortState.NoSort
        val nameToFilter = NameToFilter("")
        val sortOrFilter = Pair(sortState, nameToFilter)
        val type = Type.Good

        val result = sortFilterHabitsUseCase(sortOrFilter, type).first()
        assertThat(result, equalTo(noSortList().first()))
    }

    companion object {
        private fun createHabit(
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

        private fun listASC() = flowOf(
            listOf(createHabit(name = "listASC"))
        )

        private fun listDesc() = flowOf(
            listOf(createHabit(name = "listDesc"))
        )

        private fun noSortList() = flowOf(
            listOf(createHabit(name = "noSortList"))
        )
    }

}