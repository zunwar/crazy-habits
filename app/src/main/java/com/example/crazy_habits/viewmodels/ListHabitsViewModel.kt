package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.crazy_habits.App
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.database.habit.HabitDao
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.models.HabitModel

class ListHabitsViewModel(habitDao: HabitDao) : ViewModel() {
    private  val model : HabitModel = HabitModel(habitDao)
    private var _goodHabits : MutableLiveData<LiveData<List<HabitEntity>>> = MutableLiveData(model.getGoodHabits())
    val goodHabits: LiveData<LiveData<List<HabitEntity>>> = _goodHabits
    private val _badHabits : MutableLiveData<LiveData<List<HabitEntity>>> = MutableLiveData(model.getBadHabits())
    val badHabits : LiveData<LiveData<List<HabitEntity>>> = _badHabits

    init {
        Log.d("MVVM", "ListHabitsViewModel created")
    }

    fun filterHabitsByName(name: String) {
        if (name.isNotEmpty()) {
            _goodHabits.postValue(
                Transformations.map(model.getGoodHabits()) { list ->
                    list
                        .filter { habit ->
                            name in habit.name
                        }
                }
            )
        } else if (name.isBlank()) {
            _goodHabits.postValue(model.getGoodHabits())
        }
        if (name.isNotEmpty()) {
            _badHabits.postValue(
                Transformations.map(model.getBadHabits()) { list ->
                    list
                        .filter { habit ->
                            name in habit.name
                        }
                }
            )
        } else if (name.isBlank()) {
            _badHabits.postValue(model.getBadHabits())
        }
    }

    override fun onCleared() {
        Log.d("MVVM", "ListHabitsViewModel dead")
        super.onCleared()
    }


    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return ListHabitsViewModel(
                    (app as App).database.habitDao()
                ) as T
            }
        }
    }

}