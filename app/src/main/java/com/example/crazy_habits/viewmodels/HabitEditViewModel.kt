package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.crazy_habits.*
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.database.habit.HabitDao
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.fragments.HabitEditFragment
import com.example.crazy_habits.fragments.ListHabitsFragment.Companion.HABIT_TO_EDIT_ID
import com.example.crazy_habits.models.HabitModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class HabitEditViewModel(
    habitDao: HabitDao,
    val isEditable: Boolean,
    private var idHabit: String
) : ViewModel() {
    private val _displayOldHabit: MutableLiveData<HabitEntity> = MutableLiveData()
    val displayOldHabit: LiveData<HabitEntity> = _displayOldHabit
    private val model: HabitModel = HabitModel(habitDao)
    private var _closeFragment = SingleLiveEvent<Boolean>()
    val closeFragment = _closeFragment
    private var _navigateToColorFragment = SingleLiveEvent<Boolean>()
    val navigateToColorFragment = _navigateToColorFragment
    private var _selectedPriority: Priority = Priority.Middle
    val selectedPriority get() = _selectedPriority
    private var _colorHabit = -1
    val colorHabit get() = _colorHabit
    private var colorChange: Boolean = false

    init {
        Log.d(TAG, "HabitEditViewModel created")
        displayOldHabit()
    }

    private fun addHabit(habit: HabitEntity) {
        viewModelScope.launch {
            model.addHabit(habit)
        }
    }


    private fun displayOldHabit() {
        if (isEditable) {
            viewModelScope.launch {
                model.getHabitToEdit(idHabit).collect{
                    _displayOldHabit.postValue(it)
                }
            }
        }
    }

    private fun changeHabit(habit: HabitEntity) {
        viewModelScope.launch {
            model.changeHabit(habit)
        }
    }

    fun saveHabit(habit: HabitEntity) {
        if (isEditable) {
            changeHabit(habit)
        } else {
            addHabit(habit)
        }
        closeScreen()
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
        return idHabit
    }

    fun toColorFragmentClicked() {
        _navigateToColorFragment.value = true
    }

    override fun onCleared() {
        Log.d(TAG, "HabitEditViewModel dead")
        super.onCleared()
    }

    private fun closeScreen() {
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
                val frag = checkNotNull(extras[VIEW_MODEL_STORE_OWNER_KEY])
                val isEditable = !(frag as HabitEditFragment).requireArguments().isEmpty
                val id = if (isEditable) frag.requireArguments().getString(HABIT_TO_EDIT_ID)!! else UUID.randomUUID().toString()
                return HabitEditViewModel(
                    (app as App).database.habitDao(),
                    isEditable,
                    id
                ) as T
            }
        }
    }
}