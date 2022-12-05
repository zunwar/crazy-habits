package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.crazy_habits.App
import com.example.crazy_habits.SingleLiveEvent
import com.example.crazy_habits.database.habit.HabitDao
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.models.HabitModel

class HabitEditViewModel(habitDao: HabitDao) : ViewModel() {
    private  val model : HabitModel = HabitModel(habitDao)
    private var _habitEditFragmentCloseClicked = SingleLiveEvent<Boolean>()
    val habitEditFragmentAddClicked = _habitEditFragmentCloseClicked

    init {
        Log.d("MVVM", "HabitEditViewModel created")
    }

    fun addHabit(habit: HabitEntity) {
        model.addHabit(habit)
        closeScreen()
    }

    fun changeHabit(habit: HabitEntity) {
        model.changeHabit(habit)
        closeScreen()
    }

    fun getHabitToEdit(id: String): HabitEntity {
        return model.getHabitToEdit(id)
    }

    override fun onCleared() {
        Log.d("MVVM", "HabitEditViewModel dead")
        super.onCleared()
    }

    private fun closeScreen() {
        model.habitEditFragmentCloseClicked = true
        _habitEditFragmentCloseClicked.value = true
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val app = checkNotNull(extras[APPLICATION_KEY])
                return HabitEditViewModel(
                    (app as App).database.habitDao()
                ) as T
            }
        }
    }

}