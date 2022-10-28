package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crazy_habits.Habit
import com.example.crazy_habits.models.HabitModel

class HabitEditViewModel : ViewModel() {
    private val _habit : MutableLiveData<Habit> by lazy { MutableLiveData<Habit>() }
    private val _oldHabit : MutableLiveData<Habit> by lazy { MutableLiveData<Habit>() }
    private  val model : HabitModel = HabitModel()

    val habit : LiveData<Habit> = _habit
    val oldhabit : LiveData<Habit> = _oldHabit


    init {
        Log.d("MVVM", "viewModel created")
        load()
    }

    private fun load() {
            _habit.postValue(model.habitList.last())
    }

//    fun addHabit(habit : Habit){
//        model.addHabit(habit)
//        load()
//    }

//    fun changeHabit(habit: Habit, id: String) {
//        _habit.postValue(model.changeHabit(habit, id))
//    }


    override fun onCleared() {
        Log.d("MVVM", "viewModel dead")
        super.onCleared()
    }


}