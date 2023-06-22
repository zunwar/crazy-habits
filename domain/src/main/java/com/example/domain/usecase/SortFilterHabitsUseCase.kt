package com.example.domain.usecase

import com.example.domain.entities.Habit
import com.example.domain.entities.NameToFilter
import com.example.domain.entities.SortState
import com.example.domain.repository.HabitListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.domain.entities.Type

class SortFilterHabitsUseCase @Inject constructor(
    private val habitListRepository: HabitListRepository
    ) {

   suspend operator fun invoke(sortOrFilter: Pair<SortState, NameToFilter>, type: Type): Flow<List<Habit>> {
            return when (sortOrFilter.first) {
                SortState.SortASC -> habitListRepository.getHabitsByNameAndTypeAndSortASC(
                    sortOrFilter.second.string,
                    type
                )
                SortState.SortDESC -> habitListRepository.getHabitsByNameAndTypeAndSortDESC(
                    sortOrFilter.second.string,
                    type
                )
                SortState.NoSort -> {
                    habitListRepository.getHabitsByNameAndType(sortOrFilter.second.string, type)
                }
            }
    }
}