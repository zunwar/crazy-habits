package com.example.data.mappers

import com.example.data.entities.HabitEntity
import com.example.domain.entities.Habit

internal fun Habit.toEntity(): HabitEntity = HabitEntity(
    name,
    desc,
    type,
    priority,
    number,
    frequency,
    colorHabit,
    isSentToServer,
    date,
    id
)