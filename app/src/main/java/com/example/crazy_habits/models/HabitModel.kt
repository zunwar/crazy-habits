package com.example.crazy_habits.models

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.crazy_habits.Type
import com.example.crazy_habits.database.habit.HabitDao
import com.example.crazy_habits.database.habit.HabitEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HabitModel(private val habitDao: HabitDao) {

    init {
        Log.d("MVVM", "HabitModel created")
    }


    fun addHabit(habit: HabitEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            habitDao.insertAll(habit)
        }
    }

    fun getGoodHabits() : LiveData<List<HabitEntity>> {
        return habitDao.getHabitsByType(type = Type.Good)
    }

    fun getBadHabits() : LiveData<List<HabitEntity>> {
        return habitDao.getHabitsByType(type = Type.Bad)
    }

    fun changeHabit(habit: HabitEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            habitDao.updateHabit(habit)
        }
    }

    fun getHabitToEdit(id: String) : LiveData<HabitEntity> {
        return habitDao.getHabitById(id)
    }

    fun getHabitByName(habit: HabitEntity): LiveData<List<HabitEntity>> {
        return habitDao.getHabitsByName(habit.name)
    }

}