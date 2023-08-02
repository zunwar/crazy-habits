package com.example.presentation.listhabits.badlist

import com.example.domain.usecase.*
import com.example.presentation.listhabits.BaseListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BadListViewModel @Inject constructor(
    sortFilterHabitsUseCase: SortFilterHabitsUseCase,
    deleteHabitUseCase: DeleteHabitUseCase,
    syncHabitsWithServerUseCase: SyncHabitsWithServerUseCase,
    doHabitUseCase: DoHabitUseCase
) : BaseListViewModel(
    sortFilterHabitsUseCase,
    deleteHabitUseCase,
    syncHabitsWithServerUseCase,
    doHabitUseCase,
    isBadList = true
)