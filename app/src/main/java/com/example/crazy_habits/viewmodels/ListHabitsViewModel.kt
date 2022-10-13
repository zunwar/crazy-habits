package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crazy_habits.Habit
import com.example.crazy_habits.Type
import com.example.crazy_habits.models.HabitModel

class ListHabitsViewModel : ViewModel() {
    private val _habit : MutableLiveData<List<Habit>> by lazy { MutableLiveData<List<Habit>>() }
//    private val _oldHabit : MutableLiveData<Habit> by lazy { MutableLiveData<Habit>() }
    private  val model : HabitModel = HabitModel()

    val habit : LiveData<List<Habit>> = _habit
//    val oldhabit : LiveData<Habit> = _oldHabit


    init {
        Log.d("MVVM", "ListHabitsViewModel created")
        load()
    }

    private fun load() {
        try {
            _habit.postValue(model.getHabList())
//            model.habitList.forEach{_habit.postValue(it)}
//            Log.d("MVVM", "GoodHabitsViewModel$_habit")
        }
        catch (e : Exception) {}
    }

    private fun addHabit(habit : Habit){
        model.addHabit(habit)
        load()
    }

    fun getListOfHabits() : List<Habit>{
        return model.getHabList()
    }

    fun getGoodHabits() : List<Habit> {
        return model.getHabList().filter { it.type == Type.Good.type }
    }

    fun getBadHabits() : List<Habit> {
        return model.getHabList().filter { it.type == Type.Bad.type }
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
            false
        } else {
            addHabit(habit)
            true
        }
//        return model.isChange(habit)
    }



    override fun onCleared() {
        Log.d("MVVM", "ListHabitsViewModel dead")
        super.onCleared()
    }

}