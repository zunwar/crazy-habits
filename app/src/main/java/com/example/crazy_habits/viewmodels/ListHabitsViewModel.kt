package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crazy_habits.Habit
import com.example.crazy_habits.Type
import com.example.crazy_habits.models.HabitModel

class ListHabitsViewModel : ViewModel() {
    private val _habit : MutableLiveData<List<Habit>> = MutableLiveData<List<Habit>>()
    val habit : LiveData<List<Habit>> = _habit
    private  val model : HabitModel = HabitModel()

    init {
        Log.d("MVVM", "ListHabitsViewModel created")
        load()
    }

    private fun load() {
            _habit.postValue(model.getHabList())
    }

    private fun addHabit(habit : Habit){
        model.addHabit(habit)
        load()
    }

    fun getListOfHabits() : List<Habit>{
        return model.getHabList()
    }

    fun getGoodHabits() : List<Habit> {
        return _habit.value!!.filter { it.type == Type.Good.type }
    }

    fun getBadHabits() : List<Habit> {
        return _habit.value!!.filter { it.type == Type.Bad.type }
    }

    fun getHabitToEdit(id: String) : Habit {
        return model.getHabitToEdit(id)
    }

    private fun changeHabit(habit: Habit) {
        model.changeHabit(habit)
        load()
    }

    fun isChange(habit: Habit) : Boolean {
        return if (model.isChange(habit)) {
            changeHabit(habit)
            true
        } else {
            addHabit(habit)
            false
        }
//        return model.isChange(habit)
    }

    fun filterHabitsByName(name: String) {
        if (name.isNotEmpty()) {
            val filteredList: List<Habit> = _habit.value!!.filter { it.name.contains(name) }
            _habit.postValue(filteredList)
        } else _habit.postValue(getListOfHabits())
    }



    override fun onCleared() {
        Log.d("MVVM", "ListHabitsViewModel dead")
        super.onCleared()
    }

}