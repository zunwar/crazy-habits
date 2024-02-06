package com.example.crazy_habits.screen

import com.kaspersky.kaspresso.screens.KScreen
import com.example.presentation.R
import com.example.presentation.edithabit.HabitEditFragment
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.spinner.KSpinner
import io.github.kakaocup.kakao.spinner.KSpinnerItem
import io.github.kakaocup.kakao.text.KButton

object HabitEditFragmentScreen : KScreen<HabitEditFragmentScreen>() {

    override val layoutId: Int = R.layout.fragment_habit_edit
    override val viewClass: Class<*> = HabitEditFragment::class.java

    val name = KEditText { withId(R.id.NameHabitText) }
    val description = KEditText { withId(R.id.DescText) }
    val typeRb0 = KButton { withId(R.id.radioButton0) }
    val typeRb1 = KButton { withId(R.id.radioButton1) }
    val priority = KSpinner(
        builder = { withId(R.id.prioritySpinner) },
        itemTypeBuilder = { itemType(::KSpinnerItem) }
    )
    val number = KEditText { withId(R.id.Number) }
    val frequency = KEditText { withId(R.id.Frequency) }

    val submitButton = KButton { withId(R.id.addButton) }


}