package com.example.domain.usecase

import com.example.domain.repository.HabitListRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val habitListRepository: HabitListRepository
    ) {

    suspend operator fun invoke(idHabit: String){
        habitListRepository.deleteHabit(idHabit)
    }
}