package com.example.domain.usecase

import com.example.domain.entities.SortState
import javax.inject.Inject

class NextSortStateUseCase @Inject constructor() {

    operator fun invoke(sort: SortState): SortState = when (sort) {
        SortState.SortASC -> SortState.SortDESC
        SortState.SortDESC -> SortState.NoSort
        SortState.NoSort -> SortState.SortASC
    }

}