package com.example.crazy_habits.screen

import com.example.crazy_habits.FirstActivity
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KButton
import com.example.crazy_habits.R
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KTextView

object FirstActivityScreen : KScreen<FirstActivityScreen>() {

    override val layoutId: Int = R.layout.activity_first
    override val viewClass: Class<*> = FirstActivity::class.java

    val sortButton = KButton { withId(R.id.sort_button_tool_bar) }
    val filterButton = KButton { withId(R.id.filter_button_tool_bar) }
    val filterText = KEditText { withId(com.example.presentation.R.id.filter_text_set) }
    val bottomSheetFilterButton = KButton { withId(com.example.presentation.R.id.filter_button) }
    val bottomSheetHeader = KTextView { withId(com.example.presentation.R.id.filter_name) }
}