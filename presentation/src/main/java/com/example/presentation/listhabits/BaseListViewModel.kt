package com.example.presentation.listhabits

import androidx.lifecycle.*
import com.example.domain.entities.*
import com.example.domain.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseListViewModel(
    private val syncHabitsWithServerUseCase: SyncHabitsWithServerUseCase,
    private val sortFilterHabitsUseCase: SortFilterHabitsUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val doHabitUseCase: DoHabitUseCase,
    private val nextSortStateUseCase: NextSortStateUseCase,
) : ViewModel() {

    private val sortOrFilterStateFlow = MutableStateFlow(Pair(SortState.NoSort, NoName))
    private val _listLoadedToRecycler: MutableLiveData<Boolean> = MutableLiveData()
    val listLoadedToRecycler: LiveData<Boolean> = _listLoadedToRecycler

    fun getHabitsList(): LiveData<List<Habit>> {
        return sortOrFilterStateFlow.flatMapLatest {
            withContext(Dispatchers.IO) {
                sortFilterHabitsUseCase(sortAndFilter = it)
            }
        }.asLiveData()
    }

    fun sortClicked() {
        val sortState = nextSortStateUseCase(sortOrFilterStateFlow.value.first)
        sortOrFilterStateFlow.value = sortOrFilterStateFlow.value.copy(first = sortState)
    }

    fun updateNameToFilter(name: String) {
        val nameToFilter = NameToFilter(name)
        sortOrFilterStateFlow.value = sortOrFilterStateFlow.value.copy(second = nameToFilter)
    }

    fun listLoadedToRecycler(isLoaded: Boolean) {
        _listLoadedToRecycler.postValue(isLoaded)
    }

    fun deleteClickedHabit(idHabit: String) = liveData {
        emit(deleteHabitUseCase(idHabit))
    }

    fun syncHabitsWithServer() {
        viewModelScope.launch(Dispatchers.IO) {
            syncHabitsWithServerUseCase()
        }
    }

    fun doHabitClicked(habit: Habit) = liveData {
        emit(doHabitUseCase(habit))
    }

}