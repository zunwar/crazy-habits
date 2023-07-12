package com.example.data.entities

import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import com.squareup.moshi.Json

data class HabitDto(
    @Json(name = "title") val name: String,
    @Json(name = "description") val desc: String,
    @Json(name = "type") val type: Type,
    @Json(name = "priority") val priority: Priority,
    @Json(name = "count") val number: Int,
    @Json(name = "frequency") val frequency: Int,
    @Json(name = "color") val colorHabit: Int,
    @Json(name = "date") val date: Int,
    @Json(name = "done_dates") val done_dates: Int = 0,
)

fun HabitDto.toHabitEntity(isSentToServer: Boolean, id: String): HabitEntity = HabitEntity(
    name = this.name,
    desc = this.desc,
    type = this.type,
    priority = this.priority,
    number = this.number,
    frequency = this.frequency,
    colorHabit = this.colorHabit,
    date = this.date,
    isSentToServer = isSentToServer,
    doneCount = 0,
    id = id
)
