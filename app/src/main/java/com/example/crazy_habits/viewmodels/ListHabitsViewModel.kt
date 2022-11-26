package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.database.habit.Habit
import com.example.crazy_habits.models.HabitModel

class ListHabitsViewModel : ViewModel() {
    private val _goodHabits : MutableLiveData<List<Habit>> = MutableLiveData<List<Habit>>()
    val goodHabits : LiveData<List<Habit>> = _goodHabits
    private val _badHabits : MutableLiveData<List<Habit>> = MutableLiveData<List<Habit>>()
    val badHabits : LiveData<List<Habit>> = _badHabits
    private  val model : HabitModel = HabitModel()

    init {
        Log.d("MVVM", "ListHabitsViewModel created")
        load()
    }

    private fun load() {
        _goodHabits.postValue(model.getGoodHabits())
        Log.d(TAG, "load: ${goodHabits.value.toString()}")
        _badHabits.postValue(model.getBadHabits())
    }

    private fun addHabit(habit : Habit){
        model.addHabit(habit)
        load()
    }

    private fun getListOfHabits() : List<Habit>{
        return model.getHabList()
    }

    private fun changeHabit(habit: Habit) {
        model.changeHabit(habit)
        load()
    }

    fun update() {
        load()
    }

    fun filterHabitsByName(name: String) {
        if (name.isNotEmpty()) {
            val goodFilteredList: List<Habit> = model.getGoodHabits().filter { it.name.contains(name) }
            val badFilteredList : List<Habit> = model.getBadHabits().filter { it.name.contains(name) }
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

}