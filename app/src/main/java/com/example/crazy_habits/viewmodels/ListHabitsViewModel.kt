package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.crazy_habits.App
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.Type
import com.example.crazy_habits.database.habit.HabitDao
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.database.habit.NameToFilter
import com.example.crazy_habits.database.habit.NoName
import com.example.crazy_habits.models.HabitModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ListHabitsViewModel(habitDao: HabitDao) : ViewModel() {
    private val model: HabitModel = HabitModel(habitDao)
    private val nameToFilter = MutableStateFlow<NameToFilter>(NoName)

    init {
        Log.d(TAG, "ListHabitsViewModel created")
    }

    fun getRightHabits(badOrGood: Boolean): LiveData<List<HabitEntity>> {
        return when (badOrGood) {
            false -> getHabitsByType(Type.Good)
            true -> getHabitsByType(Type.Bad)
        }
    }

    private fun getHabitsByType(type: Type): LiveData<List<HabitEntity>> {
        return nameToFilter.flatMapLatest { name ->
            if (name == NoName) {
                model.getHabitsByType(type)
            } else {
                model.searchHabitsByNameAndType(name.string, type)
            }
        }.asLiveData()
    }

    fun updateNameToFilter(name: String) {
        nameToFilter.value = NameToFilter(name)
    }

    override fun onCleared() {
        Log.d(TAG, "ListHabitsViewModel dead")
        super.onCleared()
    }

    fun deleteClickedHabit(idd: String) {
        viewModelScope.launch {
            model.deleteHabitById(idd)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val app =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return ListHabitsViewModel(
                    (app as App).database.habitDao()
                ) as T
            }
        }
    }

}