package com.example.presentation.listhabits

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.domain.entities.*
import com.example.domain.usecase.*
import com.example.presentation.MainDispatcherRule
import com.example.presentation.getOrAwaitValue
import com.example.presentation.listhabits.goodlist.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


/**
 * Unit tests for the implementation of [GoodListViewModel].
 */
@Config
@RunWith(AndroidJUnit4::class)
class GoodListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private lateinit var goodListViewModel: GoodListViewModel
    private val sortFilterHabitsUseCase = mockk<SortFilterHabitsUseCase>()
    private val deleteHabitUseCase = mockk<DeleteHabitUseCase>()
    private val getAvatarUriUseCase = mockk<GetAvatarUriUseCase>()
    private val syncHabitsWithServerUseCase = mockk<SyncHabitsWithServerUseCase>()
    private val doHabitUseCase = mockk<DoHabitUseCase>()
    private val nextSortStateUseCase = mockk<NextSortStateUseCase>(relaxed = true)

    @ExperimentalCoroutinesApi
    @Before
    fun setupViewModel() {
        coEvery { sortFilterHabitsUseCase(any()) } returns flowOf(listOf(testHabit))
        coEvery { deleteHabitUseCase(any()) }
        coEvery { getAvatarUriUseCase() } returns flowOf(Uri.parse("www.test.com"))
        coEvery { syncHabitsWithServerUseCase() }
        coEvery { doHabitUseCase(any()) } returns Pair(MessageDoHabit.DoGoodHabitEnough, 2)

        goodListViewModel = GoodListViewModel(
            sortFilterHabitsUseCase,
            syncHabitsWithServerUseCase,
            deleteHabitUseCase,
            getAvatarUriUseCase,
            doHabitUseCase,
            nextSortStateUseCase
        )
    }

    @Test
    fun `get good habits list`() {
        val list = listOf(testHabit)
        val receivedList = goodListViewModel.getHabitsList().getOrAwaitValue()
        assertThat(receivedList, equalTo(list))
    }

    @Test
    fun `listLoadedToRecycler true`() {
        goodListViewModel.listLoadedToRecycler(true)
        val result = goodListViewModel.listLoadedToRecycler.getOrAwaitValue()
        assertThat(true, equalTo(result))
    }

    @Test
    fun `listLoadedToRecycler false`() {
        goodListViewModel.listLoadedToRecycler(false)
        val result = goodListViewModel.listLoadedToRecycler.getOrAwaitValue()
        assertThat(false, equalTo(result))
    }

    @Test
    fun `doHabitClicked get liveData back`() {
        val result = goodListViewModel.doHabitClicked(testHabit).getOrAwaitValue()
        assertThat(result, notNullValue())
    }

    @Test
    fun `getUriToAvatarDownload update and has value`() {
        val result = goodListViewModel.uri.getOrAwaitValue()
        println(goodListViewModel.uri.getOrAwaitValue())
        assertThat(result, notNullValue())
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
            isSentToServer = true,
            date = 11,
            doneCount = 1,
            id = "111",
        )
    }

}