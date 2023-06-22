package com.example.data.mappers

import com.example.data.entities.HabitEntity
import com.example.domain.entities.Habit

internal fun HabitEntity.toDomain(): Habit = Habit(
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