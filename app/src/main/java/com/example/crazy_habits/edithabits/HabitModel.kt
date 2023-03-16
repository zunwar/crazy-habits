package com.example.crazy_habits.edithabits

import android.util.Log
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.utils.Type
import com.example.crazy_habits.database.habit.HabitDao
import com.example.crazy_habits.database.habit.HabitEntity
import kotlinx.coroutines.flow.Flow

class HabitModel(private val habitDao: HabitDao) {

    init {
        Log.d(TAG, "HabitModel created")
    }

    suspend fun addHabit(habit: HabitEntity) {
        habitDao.insertAll(habit)
    }

    fun getHabitsByType(type: Type): Flow<List<HabitEntity>> {
        return habitDao.getHabitsByType(type)
    }

    suspend fun changeHabit(habit: HabitEntity) {
        habitDao.updateHabit(habit)
    }

    fun getHabitToEdit(id: String): Flow<HabitEntity> {
        return habitDao.getHabitById(id)
    }

    fun searchHabitsByNameAndType(name: String, type: Type): Flow<List<HabitEntity>> {
        return habitDao.searchHabitsByNameAndType(name, type)
    }

    suspend fun deleteHabitById(id: String) {
        habitDao.deleteById(id)
    }

}