package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.crazy_habits.App
import com.example.crazy_habits.Priority
import com.example.crazy_habits.SingleLiveEvent
import com.example.crazy_habits.database.habit.HabitDao
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.models.HabitModel
import java.util.*

class HabitEditViewModel(habitDao: HabitDao) : ViewModel() {
    private val model: HabitModel = HabitModel(habitDao)
    private var _closeFragment = SingleLiveEvent<Boolean>()
    val closeFragment = _closeFragment
    private var _navigateToColorFragment = SingleLiveEvent<Boolean>()
    val navigateToColorFragment = _navigateToColorFragment
    private var _navigateToColorFragmentWithBundle = SingleLiveEvent<Boolean>()
    val navigateToColorFragmentWithBundle = _navigateToColorFragmentWithBundle

    private var _isEditable: Boolean = false
    val isEditable get() = _isEditable
    private var _selectedPriority: Priority = Priority.Middle
    val selectedPriority get() = _selectedPriority
    private var _colorHabit = -1
    val colorHabit get() = _colorHabit
    private var idHabit = ""




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

    fun getHabitToEdit(id: String): LiveData<HabitEntity> {
        _isEditable = true
        idHabit = id
        return model.getHabitToEdit(id)
    }

    fun selectPriority(priority: Priority){
        _selectedPriority=priority
    }

    fun setColorOfHabit(color: Int){
        _colorHabit = color
    }

    fun getId(): String{
        return if (isEditable) idHabit
        else UUID.randomUUID().toString()
    }

    fun toColorFragmentClicked(){
        if (isEditable) _navigateToColorFragment.value = true
        else _navigateToColorFragmentWithBundle.value = true
    }

    override fun onCleared() {
        Log.d("MVVM", "HabitEditViewModel dead")
        super.onCleared()
    }

    private fun closeScreen() {
        _closeFragment.value = true
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