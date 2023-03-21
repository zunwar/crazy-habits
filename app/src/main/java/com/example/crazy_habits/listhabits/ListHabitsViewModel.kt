package com.example.crazy_habits.listhabits

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.crazy_habits.App
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.utils.Type
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.database.habit.NameToFilter
import com.example.crazy_habits.database.habit.NoName
import com.example.crazy_habits.edithabits.HabitModel
import com.example.crazy_habits.utils.SortState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ListHabitsViewModel(private val habitModel: HabitModel) : ViewModel() {
    private val _listLoadedToRecycler: MutableLiveData<Boolean> = MutableLiveData()
    val listLoadedToRecycler: LiveData<Boolean> = _listLoadedToRecycler
    private val sortOrFilterStateFlow = MutableStateFlow(Pair(SortState.NoSort, NoName))
    private var nameToFilterR = NoName
    private var sortState: SortState = SortState.NoSort

    init {
        Log.d(TAG, "ListHabitsViewModel---init created ")
    }

    fun getGoodOrBadList(isBadList: Boolean): LiveData<List<HabitEntity>> {
        return when (isBadList) {
            false -> getList(Type.Good)
            true -> getList(Type.Bad)
        }
    }

    private fun getList(type: Type): LiveData<List<HabitEntity>> {
        return sortOrFilterStateFlow.flatMapLatest {
            when (it.first) {
                SortState.SortASC -> habitModel.getHabitsByNameAndTypeAndSort(
                    it.second.string,
                    type,
                    1
                )
                SortState.SortDESC -> habitModel.getHabitsByNameAndTypeAndSort(
                    it.second.string,
                    type,
                    2
                )
                SortState.NoSort -> habitModel.getHabitsByNameAndType(it.second.string, type)
            }
        }.asLiveData()
    }

    fun sortClicked() {
        sortState = when (sortState) {
            SortState.SortASC -> SortState.SortDESC
            SortState.SortDESC -> SortState.NoSort
            SortState.NoSort -> SortState.SortASC
        }
        updateSortOrFilterState()
    }

    fun updateNameToFilter(name: String) {
        nameToFilterR = NameToFilter(name)
        updateSortOrFilterState()
    }

    private fun updateSortOrFilterState() {
        sortOrFilterStateFlow.value = Pair(sortState, nameToFilterR)
    }

    override fun onCleared() {
        Log.d(TAG, "ListHabitsViewModel---onCleared --dead")
        super.onCleared()
    }

    fun deleteClickedHabit(idd: String) {
        viewModelScope.launch {
            habitModel.deleteHabitById(idd)
        }
    }

    fun listLoadedToRecycler(isLoaded: Boolean) {
        _listLoadedToRecycler.postValue(isLoaded)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val app = checkNotNull(extras[APPLICATION_KEY])
                return ListHabitsViewModel(
                    (app as App).habitModel
                ) as T
            }
        }
    }

}