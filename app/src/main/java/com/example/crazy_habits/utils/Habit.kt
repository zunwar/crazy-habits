package com.example.crazy_habits.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    val name: String,
    val desc: String,
    val type: Type,
    val priority: Priority,
    val number: String,
    val period: String,
    val colorHabit: Int,
    val id: String
) : Parcelable
