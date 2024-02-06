package com.example.domain.usecase

import com.example.domain.entities.Habit
import com.example.domain.entities.NameToFilter
import com.example.domain.entities.SortState
import com.example.domain.repository.HabitListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.domain.entities.Type

class SortFilterHabitsUseCase @Inject constructor(
    private val habitListRepository: HabitListRepository,
    private val type: Type
) {

    suspend operator fun invoke(
        sortAndFilter: Pair<SortState, NameToFilter>,
    ): Flow<List<Habit>> {
        return when (sortAndFilter.first) {
            SortState.SortASC -> habitListRepository.getHabitsByNameAndTypeAndSortASC(
                sortAndFilter.second.string,
                type
            )
            SortState.SortDESC -> habitListRepository.getHabitsByNameAndTypeAndSortDESC(
                sortAndFilter.second.string,
                type
            )
            SortState.NoSort -> {
                habitListRepository.getHabitsByNameAndType(sortAndFilter.second.string, type)
            }
        }
    }
}