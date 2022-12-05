package com.example.crazy_habits.models

import android.util.Log
import com.example.crazy_habits.Type
import com.example.crazy_habits.database.habit.HabitDao
import com.example.crazy_habits.database.habit.HabitEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class HabitModel(private val habitDao: HabitDao) {
    private var _habitList: MutableList<HabitEntity> = mutableListOf()
    private val habitEditFragmentCloseClickedObservers = mutableListOf<(Boolean) -> Unit>()
    var habitEditFragmentCloseClicked: Boolean by Delegates.observable(false) { _, _, newValue ->
        habitEditFragmentCloseClickedObservers.forEach { it(newValue) }
    }

    private fun getDataFromDb(): MutableList<HabitEntity> {
        lateinit var mList: MutableList<HabitEntity>
        val job = GlobalScope.launch(Dispatchers.IO) {
            mList = habitDao.getAllHabits().toMutableList()
        }
        while (job.isActive) {}
        return mList
    }

    init {
        Log.d("MVVM", "HabitModel created")
        load()
    }

    private fun load() {
        habitEditFragmentCloseClickedObservers.add{
            _habitList = getDataFromDb()
        }
            _habitList = getDataFromDb()
    }

    fun addHabit(habit: HabitEntity) {
        _habitList = getDataFromDb()
        _habitList.add(habit)
        GlobalScope.launch(Dispatchers.IO) {
            habitDao.insertAll(_habitList)
        }
    }

    fun getGoodHabits() : List<HabitEntity> {
        return _habitList.filter { it.type == Type.Good }
    }

    fun getBadHabits() : List<HabitEntity> {
        return  _habitList.filter { it.type == Type.Bad }
    }

    fun getHabList() : List<HabitEntity> {
        return _habitList
    }

    fun changeHabit(habit: HabitEntity) {
        load()
        val habitToChange = _habitList.find{it.id == habit.id}!!
        val habitToChangeIndex = _habitList.indexOf(habitToChange)
        _habitList[habitToChangeIndex] = habit
        GlobalScope.launch(Dispatchers.IO) {
            habitDao.insertAll(_habitList)
        }
    }

    fun getHabitToEdit(id: String) : HabitEntity {
        return _habitList.find{it.id == id}!!
    }

}