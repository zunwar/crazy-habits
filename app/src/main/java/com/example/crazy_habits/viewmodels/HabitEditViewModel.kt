package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.crazy_habits.*
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.database.habit.HabitDao
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.models.HabitModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class HabitEditViewModel(habitDao: HabitDao) : ViewModel() {
    private val model: HabitModel = HabitModel(habitDao)
    private var _closeFragment = SingleLiveEvent<Boolean>()
    val closeFragment = _closeFragment
    private var _navigateToColorFragment = SingleLiveEvent<Boolean>()
    val navigateToColorFragment = _navigateToColorFragment
    private var _isEditable: Boolean = false
    val isEditable get() = _isEditable
    private var _selectedPriority: Priority = Priority.Middle
    val selectedPriority get() = _selectedPriority
    private var _colorHabit = -1
    val colorHabit get() = _colorHabit
    private var idHabit = ""
    private var colorChange: Boolean = false

    init {
        Log.d(TAG, "HabitEditViewModel created")
    }

    private fun addHabit(habit: HabitEntity) {
        viewModelScope.launch {
            model.addHabit(habit)
        }
    }

    private fun changeHabit(habit: HabitEntity) {
        viewModelScope.launch {
            model.changeHabit(habit)
        }
    }

    fun saveHabit(habit: HabitEntity) {
        if (_isEditable) {
            changeHabit(habit)
        } else {
            addHabit(habit)
        }
    }

    fun getHabitToEditFlow(id: String): Flow<HabitEntity> {
        _isEditable = true
        idHabit = id
        return model.getHabitToEdit(id)
    }

    fun selectPriority(priority: Priority) {
        _selectedPriority = priority
    }

    fun setColorFromColorFragment(color: Int) {
        colorChange = true
        _colorHabit = color
    }

    fun setColorOfHabit(color: Int) {
        if (!colorChange) _colorHabit = color
    }

    fun getId(): String {
        return if (isEditable) idHabit
        else {
            idHabit = UUID.randomUUID().toString()
            idHabit
        }
    }

    fun toColorFragmentClicked() {
        _navigateToColorFragment.value = true
    }

    override fun onCleared() {
        Log.d(TAG, "HabitEditViewModel dead")
        super.onCleared()
    }

    fun closeScreen() {
        _closeFragment.value = true
    }

    fun deleteHabitById() {
        viewModelScope.launch {
            model.deleteHabitById(idHabit)
        }
    }

    fun setRightTabItem(type: Type): Int {
        return when (type) {
            Type.Good -> 0
            Type.Bad -> 1
        }
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