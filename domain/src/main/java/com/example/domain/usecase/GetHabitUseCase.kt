package com.example.domain.usecase

import com.example.domain.entities.Habit
import com.example.domain.repository.HabitEditRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetHabitUseCase @Inject constructor(
    private val habitEditRepository: HabitEditRepository
    ) {

    suspend operator fun invoke(idHabit: String): Flow<Habit> {
        return habitEditRepository.getHabitToEdit(idHabit)
    }
}