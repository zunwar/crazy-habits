package com.example.presentation.listhabits

import android.net.Uri
import androidx.lifecycle.*
import com.example.domain.entities.*
import com.example.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ListHabitsViewModel @Inject constructor(
    private val sortFilterHabitsUseCase: SortFilterHabitsUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val getAvatarUriUseCase: GetAvatarUriUseCase,
    private val syncHabitsWithServerUseCase: SyncHabitsWithServerUseCase,
    private val doHabitUseCase: DoHabitUseCase,
    ) : ViewModel() {
    private val _listLoadedToRecycler: MutableLiveData<Boolean> = MutableLiveData()
    val listLoadedToRecycler: LiveData<Boolean> = _listLoadedToRecycler
    private val sortOrFilterStateFlow = MutableStateFlow(Pair(SortState.NoSort, NoName))
    private val _uri: MutableLiveData<Uri> = MutableLiveData()
    val uri: LiveData<Uri> = _uri

    init {
        getUriToAvatarDownload()
    }

    fun getGoodOrBadList(isBadList: Boolean): LiveData<List<Habit>> {
        return when (isBadList) {
            false -> getList(Type.Good)
            true -> getList(Type.Bad)
        }
    }

    private fun getList(type: Type): LiveData<List<Habit>> {
        return sortOrFilterStateFlow.flatMapLatest {
            withContext(Dispatchers.IO) {
                sortFilterHabitsUseCase(it, type)
            }
        }.asLiveData()
    }

    fun sortClicked() {
        val sortState: SortState = when (sortOrFilterStateFlow.value.first) {
            SortState.SortASC -> SortState.SortDESC
            SortState.SortDESC -> SortState.NoSort
            SortState.NoSort -> SortState.SortASC
        }
        sortOrFilterStateFlow.value = sortOrFilterStateFlow.value.copy(first = sortState)
    }

    fun updateNameToFilter(name: String) {
        val nameToFilter = NameToFilter(name)
        sortOrFilterStateFlow.value = sortOrFilterStateFlow.value.copy(second = nameToFilter)
    }

    fun deleteClickedHabit(idHabit: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteHabitUseCase(idHabit)
        }
    }

    fun listLoadedToRecycler(isLoaded: Boolean) {
        _listLoadedToRecycler.postValue(isLoaded)
    }

    private fun getUriToAvatarDownload() {
        viewModelScope.launch {
            getAvatarUriUseCase().collect { _uri.postValue(it) }
        }
    }

    fun syncHabitsWithServer(isBadList: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            when (isBadList) {
                false -> syncHabitsWithServerUseCase(Type.Good)
                true -> syncHabitsWithServerUseCase(Type.Bad)
            }
        }
    }

    fun doHabitClicked(habit: Habit) = liveData {
        emit(doHabitUseCase(habit))
    }

}