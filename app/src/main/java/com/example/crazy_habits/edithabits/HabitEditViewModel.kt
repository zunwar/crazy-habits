package com.example.crazy_habits.edithabits

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.crazy_habits.*
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.utils.Priority
import com.example.crazy_habits.utils.Type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class HabitEditViewModel(
    private val habitModel: HabitModel,
    id: String?
) : ViewModel() {
    private val _displayOldHabit: MutableLiveData<HabitEntity> = MutableLiveData()
    val displayOldHabit: LiveData<HabitEntity> = _displayOldHabit
    private var _closeFragment = SingleLiveEvent<Boolean>()
    val closeFragment = _closeFragment
    private var _navigateToColorFragment = SingleLiveEvent<Boolean>()
    val navigateToColorFragment = _navigateToColorFragment
    private var _selectedPriority: Priority = Priority.Middle
    val selectedPriority get() = _selectedPriority
    private var _colorHabit = -1
    val colorHabit get() = _colorHabit
    private var colorChange: Boolean = false
    private val idHabit: String = id ?: UUID.randomUUID().toString()
    val isEditable = id != null

    init {
        Log.d(TAG, "HabitEditViewModel created")
        displayOldHabit()
    }

    private fun addHabit(habit: HabitEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            habitModel.addHabit(habit)
        }
    }


    private fun displayOldHabit() {
        if (isEditable) {
            viewModelScope.launch(Dispatchers.IO) {
                habitModel.getHabitToEdit(idHabit).collect{
                    _displayOldHabit.postValue(it)
                }
            }
        }
    }

    private fun changeHabit(habit: HabitEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            habitModel.changeHabit(habit)
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
        viewModelScope.launch(Dispatchers.IO) {
            habitModel.deleteHabitById(idHabit)
        }
    }

    fun setRightTabItem(type: Type): Int {
        return when (type) {
            Type.Good -> 0
            Type.Bad -> 1
        }
    }

    companion object {
        fun provideFactory(
            id: String?
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    val app = checkNotNull(extras[APPLICATION_KEY])
                    return HabitEditViewModel(
                        (app as App).habitModel,
                        id
                    ) as T
                }
            }
    }
}