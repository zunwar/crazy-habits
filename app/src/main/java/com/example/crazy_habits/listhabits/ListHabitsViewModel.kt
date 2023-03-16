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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ListHabitsViewModel(private val habitModel: HabitModel) : ViewModel() {
    private val nameToFilter = MutableStateFlow<NameToFilter>(NoName)
    private val _listLoadedToRecycler: MutableLiveData<Boolean> = MutableLiveData()
    val listLoadedToRecycler: LiveData<Boolean> = _listLoadedToRecycler

    init {
        Log.d(TAG, "ListHabitsViewModel---created ")
    }

    fun getRightHabits(isBadList: Boolean): LiveData<List<HabitEntity>> {
        return when (isBadList) {
            false -> getHabitsByType(Type.Good)
            true -> getHabitsByType(Type.Bad)
        }
    }

    private fun getHabitsByType(type: Type): LiveData<List<HabitEntity>> {
        return nameToFilter.flatMapLatest { name ->
            if (name == NoName) {
                habitModel.getHabitsByType(type)
            } else {
                habitModel.searchHabitsByNameAndType(name.string, type)
            }
        }.asLiveData()
    }

    fun updateNameToFilter(name: String) {
        nameToFilter.value = NameToFilter(name)
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