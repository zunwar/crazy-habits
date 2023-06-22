package com.example.domain.usecase

import com.example.domain.entities.Habit
import com.example.domain.repository.HabitEditRepository
import javax.inject.Inject

class ChangeHabitUseCase @Inject constructor(private val habitEditRepository: HabitEditRepository){

    suspend operator fun invoke(habit: Habit): String{
        return habitEditRepository.changeHabit(habit)
    }
}