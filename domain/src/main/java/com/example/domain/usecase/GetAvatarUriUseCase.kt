package com.example.domain.usecase

import android.net.Uri
import com.example.domain.repository.HabitListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAvatarUriUseCase @Inject constructor(
    private val habitListRepository: HabitListRepository,
) {

    suspend operator fun invoke(): Flow<Uri> {
        return habitListRepository.getAvatar()
    }
}