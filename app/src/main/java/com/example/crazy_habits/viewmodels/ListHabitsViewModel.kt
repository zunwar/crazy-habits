package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.crazy_habits.App
import com.example.crazy_habits.database.habit.HabitDao
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.models.HabitModel

class ListHabitsViewModel(habitDao: HabitDao) : ViewModel() {
    private val _goodHabits : MutableLiveData<List<HabitEntity>> = MutableLiveData<List<HabitEntity>>()
    val goodHabits : LiveData<List<HabitEntity>> = _goodHabits
    private val _badHabits : MutableLiveData<List<HabitEntity>> = MutableLiveData<List<HabitEntity>>()
    val badHabits : LiveData<List<HabitEntity>> = _badHabits
    private  val model : HabitModel = HabitModel(habitDao)

    init {
        Log.d("MVVM", "ListHabitsViewModel created")
        load()
    }

    private fun load() {
        _goodHabits.postValue(model.getGoodHabits())
        _badHabits.postValue(model.getBadHabits())
    }

    fun update() {
        model.habitEditFragmentCloseClicked = true
        load()
    }

    fun filterHabitsByName(name: String) {
        if (name.isNotEmpty()) {
            val goodFilteredList: List<HabitEntity> = model.getGoodHabits().filter { it.name.contains(name) }
            val badFilteredList : List<HabitEntity> = model.getBadHabits().filter { it.name.contains(name) }
            _goodHabits.postValue(goodFilteredList)
            _badHabits.postValue(badFilteredList)
        } else {
            _goodHabits.postValue(model.getGoodHabits())
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