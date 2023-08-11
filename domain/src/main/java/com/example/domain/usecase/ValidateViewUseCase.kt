package com.example.domain.usecase

import javax.inject.Inject

class ValidateViewUseCase @Inject constructor(
){
    operator fun invoke(textOfView: String?): Boolean {
       return !textOfView.isNullOrEmpty()
    }
}