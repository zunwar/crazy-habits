package com.example.domain.usecase

import com.example.domain.entities.Type
import com.example.domain.repository.HabitListRepository
import javax.inject.Inject

class SyncHabitsWithServerUseCase @Inject constructor(
    private val habitListRepository: HabitListRepository,
    private val type: Type
) {

    suspend operator fun invoke() {
        habitListRepository.syncHabitsWithServer(type)
    }
}