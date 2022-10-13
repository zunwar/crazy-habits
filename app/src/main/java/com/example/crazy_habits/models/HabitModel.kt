package com.example.crazy_habits.models

import android.util.Log
import com.example.crazy_habits.Habit
import com.example.crazy_habits.utils.ContextSharedPrefs

class HabitModel{
    private val sharedPrfs = ContextSharedPrefs.getcontextSharedPrefs()
//    private val _habitList : MutableList<Habit> by lazy { getFromPrfs() }
    private var _habitList : MutableList<Habit> = getFromPrfs()
    val habitList : List<Habit> = _habitList


    private fun getFromPrfs(): MutableList<Habit> {
        val habitFromPrefsGson = sharedPrfs.getList<MutableList<Habit>>("habitListFull")
        return habitFromPrefsGson ?: mutableListOf()
    }

    init {
        Log.d("MVVM", "model created")

    }

//
//    private fun getList(): MutableList<Habit> {
//        return _habitList
//    }

    fun getHabList() : List<Habit> {
        return _habitList
    }

    fun addHabit(habit: Habit) {
        _habitList = getFromPrfs()
//        if (_habitList.isNotEmpty()) {
//            if (_habitList.last().id != habit.id){
//                _habitList.add(habit)
//                sharedPrfs.put(_habitList, "habitListFull")
//            }
//        }
//        else {
            _habitList.add(habit)
            sharedPrfs.put(_habitList, "habitListFull")
//        }
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

    fun isChange(habit : Habit) : Boolean{
        _habitList = getFromPrfs()
        return _habitList.find { it.id == habit.id } != null
    }



//    private fun loadHabitNameList() : MutableList<String>{
////        _habitNameList.add("")
//        return _habitList
//    }

}