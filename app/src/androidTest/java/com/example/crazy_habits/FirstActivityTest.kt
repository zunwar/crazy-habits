package com.example.crazy_habits

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import com.example.data.EspressoIdlingResource
import com.example.data.HabitEditRepositoryImpl
import com.example.data.local.database.HabitDao
import com.example.domain.entities.Habit
import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.example.presentation.R
import com.example.presentation.listhabits.PresentationEspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import java.util.UUID
import javax.inject.Inject

@HiltAndroidTest
@LargeTest
class FirstActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var habitEditRepositoryImpl: HabitEditRepositoryImpl
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Inject
    lateinit var habitDao: HabitDao

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(PresentationEspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(PresentationEspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }


    @Test
    fun editHabit() = runBlocking {
           habitEditRepositoryImpl.addHabit(createHabit(name = "cool habit", desc = "walk tomorrow"))
           val activityScenario = ActivityScenario.launch(FirstActivity::class.java)
           dataBindingIdlingResource.monitorActivity(activityScenario)
           onView(withText("cool habit")).perform(click())
           //verify that all the data is correct
           onView(withId(R.id.NameHabitText)).check(matches(withText("cool habit")))
           onView(withId(R.id.DescText)).check(matches(withText("walk tomorrow")))
           onView(withId(R.id.radioButton0)).check(matches(isChecked()))
           onView(withId(R.id.prioritySpinner)).check(matches(withSpinnerText(Priority.Middle.toString())))
           onView(withId(R.id.Number)).check(matches(withText("2")))
           onView(withId(R.id.Frequency)).check(matches(withText("3")))
           // edit and save
           onView(withId(R.id.NameHabitText)).perform(replaceText("super habit"))
           onView(withId(R.id.DescText)).perform(replaceText("WALK RIGHT NOW"))
           onView(withId(R.id.radioButton1)).perform(click())
           onView(withId(R.id.prioritySpinner)).perform(click())
           onView(withText("Высокий")).perform(click())
           onView(withId(R.id.Number)).perform(replaceText("10"))
           onView(withId(R.id.Frequency)).perform(replaceText("20"))
           onView(withId(R.id.addButton)).perform(scrollTo(), click())
           //verify habit is displayed on screen in the habit list
           onView(withText("super habit")).check(matches(isDisplayed()))
           onView(withText("вредная")).check(matches(isDisplayed()))
           onView(withText("20")).check(matches(isDisplayed()))
           onView(withText("Высокий")).check(matches(isDisplayed()))
           onView(withText("WALK RIGHT NOW")).check(matches(isDisplayed()))
           onView(withText("cool habit")).check(doesNotExist())
           activityScenario.close()
           clearOneBadHabit()
    }

    @Test
    fun createOneHabit_deleteHabit() = runBlocking {
        val activityScenario = ActivityScenario.launch(FirstActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)
        // create new habit
        val name = UUID.randomUUID().toString().take(5)
        onView(withId(R.id.add_new_habit_fab)).perform(click())
        onView(withId(R.id.NameHabitText)).perform(typeText(name))
        onView(withId(R.id.DescText)).perform(typeText("this is new habit"), closeSoftKeyboard())
        onView(withId(R.id.radioButton0)).check(matches(isChecked()))
        onView(withId(R.id.radioButton1)).perform(click())
        onView(withId(R.id.radioButton0)).perform(click())
        onView(withId(R.id.prioritySpinner)).perform(click())
        onView(withText("Высокий")).perform(click())
        onView(withId(R.id.Number)).perform(typeText("1"), closeSoftKeyboard())
        onView(withId(R.id.Frequency)).perform((typeText("2")), closeSoftKeyboard())
        //save habit
        onView(withId(R.id.addButton)).perform(scrollTo(), click())
        //check habit
        onView(withText(name)).check(matches(isCompletelyDisplayed()))
        //delete habit
        onView(withText(name)).perform(longClick())
        //verify habit was deleted
        onView(withText("hello")).check(doesNotExist())
        activityScenario.close()
    }

    @Test
    fun clearOneGoodHabit() {
        val activityScenario = ActivityScenario.launch(FirstActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)
        onView(withText("полезная")).perform(longClick())
        activityScenario.close()
    }

    @Test
    fun clearOneBadHabit() {
        val activityScenario = ActivityScenario.launch(FirstActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)
        onView(withText("evil")).perform(click())
        onView(withText("вредная")).perform(longClick())
        activityScenario.close()
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