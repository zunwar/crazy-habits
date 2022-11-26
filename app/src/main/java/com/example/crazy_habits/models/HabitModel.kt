package com.example.crazy_habits.models

import android.util.Log
import com.example.crazy_habits.database.habit.Habit
import com.example.crazy_habits.Type
import com.example.crazy_habits.utils.ContextSharedPrefs

class HabitModel{
    private val sharedPrfs = ContextSharedPrefs.getcontextSharedPrefs()
//    private val _habitList : MutableList<Habit> by lazy { getFromPrfs() }
    private var _habitList : MutableList<Habit> = mutableListOf()

    private fun getFromPrfs(): MutableList<Habit> {
        val habitFromPrefsGson = sharedPrfs.getList<MutableList<Habit>>("habitListFull")
        return habitFromPrefsGson ?: mutableListOf()
    }

    init {
        Log.d("MVVM", "model created")
        load()
    }

    private fun load() {
        _habitList = getFromPrfs()
    }

    fun getHabList() : List<Habit> {
        return _habitList
    }

    fun getGoodHabits() : List<Habit> {
        load()
        return _habitList.filter { it.type == Type.Good }
    }

    fun getBadHabits() : List<Habit> {
        load()
        return  _habitList.filter { it.type == Type.Bad }
    }

    fun addHabit(habit: Habit) {
        _habitList = getFromPrfs()
        _habitList.add(habit)
        sharedPrfs.put(_habitList, "habitListFull")
    }

    fun changeHabit(habit: Habit) {
        _habitList = getFromPrfs()
        val habitToChange = _habitList.find{it.id == habit.id}!!
        val habitToChangeIndex = _habitList.indexOf(habitToChange)
        _habitList[habitToChangeIndex] = habit
        sharedPrfs.put(_habitList, "habitListFull")
    }


    fun getHabitToEdit(id: String) : Habit {
        _habitList = getFromPrfs()
        return _habitList.find{it.id == id}!!
    }

}