package com.example.data.entities

import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import com.squareup.moshi.Json

data class HabitDto(
    @Json(name = "title") val name: String,
    @Json(name = "description") val desc: String,
    @Json(name = "type") val type: Int,
    @Json(name = "priority") val priority: Int,
    @Json(name = "count") val number: Int,
    @Json(name = "frequency") val frequency: Int,
    @Json(name = "color") val colorHabit: Int,
    @Json(name = "date") val date: Int,
    @Json(name = "uid") val uid: String? = null,
    @Json(name = "done_dates") val done_dates: List<Int> = listOf(0),
)

fun HabitDto.toHabitEntity(isSentToServer: Boolean, id: String): HabitEntity = HabitEntity(
    name = this.name,
    desc = this.desc,
    type = when (type) {
        0 -> Type.Good
        1 -> Type.Bad
        else -> Type.Good
    },
    priority = when (priority) {
        0 -> Priority.High
        1 -> Priority.Middle
        2 -> Priority.Low
        else -> Priority.Middle
    },
    number = this.number,
    frequency = this.frequency,
    colorHabit = this.colorHabit,
    date = this.date,
    isSentToServer = isSentToServer,
    doneCount = 0,
    id = id
)
