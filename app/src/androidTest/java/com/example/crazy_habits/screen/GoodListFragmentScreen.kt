package com.example.crazy_habits.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.example.presentation.R
import com.example.presentation.listhabits.goodlist.GoodListFragment
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.tabs.KTabLayout
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object GoodListFragmentScreen : KScreen<GoodListFragmentScreen>() {

    override val layoutId: Int = R.layout.fragment_good_list
    override val viewClass: Class<*> = GoodListFragment::class.java


    val fab = KButton { withId(R.id.add_new_habit_fab) }
    val pageTitle = KTabLayout { withId(R.id.tab_layout) }
    val rvListHabits = KRecyclerView(
        builder = { withId(R.id.recyclerView) },
        itemTypeBuilder = { itemType(::HabitListScreenItem) }
    )

    fun checkPageTitle(title: String) {
        pageTitle {
            isDisplayed()
            hasSelectedText(title)
        }
    }

    class HabitListScreenItem(matcher: Matcher<View>) :
        KRecyclerItem<HabitListScreenItem>(matcher) {
        val tvHabitName = KTextView(matcher) { withId(R.id.nameHabit) }
        val tvHabitDescription = KTextView(matcher) { withId(R.id.description) }
        val tvHabitType = KTextView(matcher) { withId(R.id.type) }
        val tvHabitFrequency = KTextView(matcher) { withId(R.id.frequency) }
        val tvHabitPriority = KTextView(matcher) { withId(R.id.priority) }
    }

}