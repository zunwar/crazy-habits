package com.example.presentation.listhabits.badlist

import com.example.domain.di.DomainModule
import com.example.domain.usecase.*
import com.example.presentation.listhabits.BaseListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BadListViewModel @Inject constructor(
    @DomainModule.BadHabitFilter sortFilterHabitsUseCase: SortFilterHabitsUseCase,
    @DomainModule.BadHabitSync syncHabitsWithServerUseCase: SyncHabitsWithServerUseCase,
    deleteHabitUseCase: DeleteHabitUseCase,
    doHabitUseCase: DoHabitUseCase,
    nextSortStateUseCase: NextSortStateUseCase
) : BaseListViewModel(
    syncHabitsWithServerUseCase,
    sortFilterHabitsUseCase,
    deleteHabitUseCase,
    doHabitUseCase,
    nextSortStateUseCase,
)