package com.example.domain.usecase

import com.example.domain.entities.DeleteStatus
import com.example.domain.repository.HabitListRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val habitListRepository: HabitListRepository
) {

    suspend operator fun invoke(idHabit: String): DeleteStatus {
        val response = habitListRepository.deleteHabit(idHabit)
        return if (response.contains("Success")){
            DeleteStatus.Deleted
        } else DeleteStatus.ErrorOccurred
    }
}