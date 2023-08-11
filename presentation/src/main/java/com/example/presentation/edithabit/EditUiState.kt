package com.example.presentation.edithabit

import com.example.domain.entities.Priority
import com.example.domain.entities.Type

data class EditUiState(
    val isLoading: Boolean = false,
    val serverResponse: String? = null,
    val nameText: String? = null,
    val isErrorName: Boolean = true,
    val descText: String? = null,
    val isErrorDesc: Boolean = true,
    val numberText: String? = null,
    val isErrorNumber: Boolean = true,
    val frequencyText: String? = null,
    val isErrorFrequency: Boolean = true,
    val type: Type? = null,
    val priority: Priority? = null,
    )