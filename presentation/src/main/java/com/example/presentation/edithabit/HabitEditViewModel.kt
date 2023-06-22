package com.example.presentation.edithabit

import androidx.lifecycle.*
import com.example.crazy_habits.*
import com.example.domain.entities.Habit
import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import com.example.domain.usecase.AddHabitUseCase
import com.example.domain.usecase.ChangeHabitUseCase
import com.example.domain.usecase.GetHabitUseCase
import com.example.presentation.edithabit.di.HabitEditModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HabitEditViewModel @Inject constructor(
    @HabitEditModule.IdHabit private val idHabit: String?,
    private val addHabitUseCase: AddHabitUseCase,
    private val getHabitUseCase: GetHabitUseCase,
    private val changeHabitUseCase: ChangeHabitUseCase,
) : ViewModel() {
    private val _displayOldHabit: MutableLiveData<Habit> = MutableLiveData()
    val displayOldHabit: LiveData<Habit> = _displayOldHabit
    private var _closeFragment = SingleLiveEvent<Boolean>()
    val closeFragment = _closeFragment
    private var _navigateToColorFragment = SingleLiveEvent<Boolean>()
    val navigateToColorFragment = _navigateToColorFragment
    private var _selectedPriority = Priority.Middle
    val selectedPriority get() = _selectedPriority
    private var _colorHabit = -1
    val colorHabit get() = _colorHabit
    private var colorChange: Boolean = false
    val isEditable = idHabit != null
    private var serverResponse: String = ""

    init {
        displayOldHabit()
    }

    fun saveHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isEditable) {
                changeHabit(habit)
            } else {
                addHabit(habit)
            }
            withContext(Dispatchers.Main) { closeScreen() }
        }
    }

    private suspend fun addHabit(habit: Habit) {
        serverResponse = addHabitUseCase(habit)
    }

    private suspend fun changeHabit(habit: Habit) {
        serverResponse = changeHabitUseCase(habit)
    }

    private fun displayOldHabit() {
        if (isEditable) {
            viewModelScope.launch(Dispatchers.IO) {
                getHabitUseCase(idHabit!!).collect {
                    _displayOldHabit.postValue(it)
                }
            }
        }
    }

    fun getServerResponse(): String {
        return serverResponse
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
        return if (isEditable) idHabit!! else UUID.randomUUID().toString()
    }

    fun toColorFragmentClicked() {
        _navigateToColorFragment.value = true
    }

    private fun closeScreen() {
        _closeFragment.value = true
    }

    fun setRightTabItem(type: Type): Int {
        return when (type) {
            Type.Good -> 0
            Type.Bad -> 1
        }
    }

}