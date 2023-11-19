package com.example.crazy_habits


import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.example.data.HabitEditRepositoryImpl
import com.example.domain.entities.Habit
import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import com.example.presentation.listhabits.goodlist.GoodListFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@MediumTest
class GoodListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var habitEditRepositoryImpl: HabitEditRepositoryImpl

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun habitDisplayedInUi() = runBlocking {
        //add habit
        val habit = createHabit("good")
        habitEditRepositoryImpl.addHabit(habit)
        //launch fragment
        val navController = TestNavHostController(getApplicationContext())
        launchFragment(navController)
        //habit is displayed
        onView(withText("good")).check(matches(isDisplayed()))
        //clear
        onView(withText("полезная")).perform(ViewActions.longClick())
        return@runBlocking
    }

    @Test
    fun simpleTry() = runBlocking{
        val navController = TestNavHostController(getApplicationContext())
        launchFragment(navController)
        delay(5000)
    }

    private fun launchFragment(navController: TestNavHostController){
        launchFragmentInHiltContainer<GoodListFragment>(fragmentArgs = null) {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.nav_graph)
            Navigation.setViewNavController(requireView(), navController)
        }
    }



    companion object {
        fun createHabit(
            name: String = "aa",
            desc: String = "bb",
            type: Type = Type.Good,
            priority: Priority = Priority.Middle,
            number: Int = 2,
            frequency: Int = 3,
            colorHabit: Int = -1,
            isSentToServer: Boolean = false,
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