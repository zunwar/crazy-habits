package com.example.domain.entities

data class Habit(
    val name: String,
    val desc: String,
    val type: Type,
    val priority: Priority,
    val number: Int,
    val frequency: Int,
    val colorHabit: Int,
    val isSentToServer: Boolean = false,
    val date: Int,
    val id: String
)

@JvmInline
value class NameToFilter(val string: String)
val NoName = NameToFilter("")