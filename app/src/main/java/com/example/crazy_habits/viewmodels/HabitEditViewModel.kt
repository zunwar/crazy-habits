package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.crazy_habits.database.habit.Habit
import com.example.crazy_habits.models.HabitModel

class HabitEditViewModel : ViewModel() {
    private  val model : HabitModel = HabitModel()

    init {
        Log.d("MVVM", "HabitEditViewModel created")
        load()
    }

    private fun load() {
    }

    fun addHabit(habit : Habit){
        model.addHabit(habit)
    }

    fun changeHabit(habit: Habit) {
        model.changeHabit(habit)
    }

    fun getHabitToEdit(id: String) : Habit {
        return model.getHabitToEdit(id)
    }

    override fun onCleared() {
        Log.d("MVVM", "HabitEditViewModel dead")
        super.onCleared()
    }

}