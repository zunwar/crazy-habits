package com.example.domain.repository

import com.example.domain.entities.Habit
import kotlinx.coroutines.flow.Flow

interface HabitEditRepository {

    suspend fun changeHabit(habit: Habit): String

    suspend fun addHabit(habit: Habit): String

    suspend fun getHabitToEdit(idHabit: String): Flow<Habit>

}