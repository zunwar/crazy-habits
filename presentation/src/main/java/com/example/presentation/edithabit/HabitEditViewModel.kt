package com.example.presentation.edithabit

import androidx.lifecycle.*
import com.example.crazy_habits.*
import com.example.domain.entities.Habit
import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import com.example.domain.usecase.AddHabitUseCase
import com.example.domain.usecase.ChangeHabitUseCase
import com.example.domain.usecase.GetHabitUseCase
import com.example.domain.usecase.ValidateViewUseCase
import com.example.presentation.edithabit.di.HabitEditModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HabitEditViewModel @Inject constructor(
    @HabitEditModule.IdHabit private val idHabit: String?,
    @HabitEditModule.IsEditable val isEditable: Boolean,
    private val addHabitUseCase: AddHabitUseCase,
    private val getHabitUseCase: GetHabitUseCase,
    private val changeHabitUseCase: ChangeHabitUseCase,
    private val validateViewUseCase: ValidateViewUseCase
) : ViewModel() {
    private val _closeFragment = SingleLiveEvent<Boolean>()
    val closeFragment = _closeFragment
    private val _navigateToColorFragment = SingleLiveEvent<Boolean>()
    val navigateToColorFragment = _navigateToColorFragment
    private var _colorHabit = -1
    val colorHabit get() = _colorHabit
    private var colorChange: Boolean = false
    val serverResponse by lazy { _uiState.value!!.serverResponse }

    private val _uiState: MutableLiveData<EditUiState> = MutableLiveData(EditUiState())
    val uiState: LiveData<EditUiState> = _uiState

    fun saveHabit() {
        viewModelScope.launch(Dispatchers.IO) {
            val habit = gatherHabit()
            if (isEditable) {
                changeHabit(habit)
            } else {
                addHabit(habit)
            }
            withContext(Dispatchers.Main) { closeScreen() }
        }
    }

    private suspend fun addHabit(habit: Habit) {
        _uiState.postValue(_uiState.value!!.copy(serverResponse = addHabitUseCase(habit)))
    }

    private suspend fun changeHabit(habit: Habit) {
        _uiState.postValue(_uiState.value!!.copy(serverResponse = changeHabitUseCase(habit)))
    }

    fun displayOldHabit(): LiveData<Habit> {
        return flow {
            if (isEditable) {
                _uiState.value = _uiState.value!!.copy(isErrorName = false)
                _uiState.value = _uiState.value!!.copy(isErrorDesc = false)
                _uiState.value = _uiState.value!!.copy(isErrorNumber = false)
                _uiState.value = _uiState.value!!.copy(isErrorFrequency = false)
                emit(getHabitUseCase(idHabit!!).first())
            }
        }.asLiveData()
    }

    fun setColorFromColorFragment(color: Int) {
        colorChange = true
        _colorHabit = color
    }

    fun setColorOfHabit(color: Int) {
        if (!colorChange) _colorHabit = color
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

    private fun validate(textOfView: String): Boolean {
        return validateViewUseCase(textOfView)
    }

    fun setNameAndValidate(name: String) {
        if (validate(name)) {
            _uiState.value = _uiState.value!!.copy(isErrorName = false)
            _uiState.value = (_uiState.value!!.copy(nameText = name))
        } else _uiState.value = _uiState.value!!.copy(isErrorName = true)
    }

    fun setDescAndValidate(desc: String) {
        if (validate(desc)) {
            _uiState.value = _uiState.value!!.copy(isErrorDesc = false)
            _uiState.value = _uiState.value!!.copy(descText = desc)
        } else _uiState.value = _uiState.value!!.copy(isErrorDesc = true)
    }

    fun setNumberAndValidate(number: String) {
        if (validate(number)) {
            _uiState.value = _uiState.value!!.copy(isErrorNumber = false)
            _uiState.value = (_uiState.value!!.copy(numberText = number))
        } else _uiState.value = _uiState.value!!.copy(isErrorNumber = true)
    }

    fun setFrequencyAndValidate(frequency: String) {
        if (validate(frequency)) {
            _uiState.value = _uiState.value!!.copy(isErrorFrequency = false)
            _uiState.value = (_uiState.value!!.copy(frequencyText = frequency))
        } else _uiState.value = _uiState.value!!.copy(isErrorFrequency = true)
    }

    fun setType(type: Type){
        _uiState.value = _uiState.value!!.copy(type = type)
    }

    fun setPriority(priority: Priority){
        _uiState.value = _uiState.value!!.copy(priority = priority)
    }

    fun getId(): String {
        return if (isEditable) idHabit!! else UUID.randomUUID().toString()
    }

    private fun getTime(): Int {
        val formatter = DateTimeFormatter.ofPattern("ddHHmss")
        return LocalDateTime.now(ZoneId.of("UTC+5")).format(formatter).toInt()
    }

    private fun gatherHabit(): Habit {
        return Habit(
            name = _uiState.value!!.nameText!!,
            desc = _uiState.value!!.descText!!,
            type = _uiState.value!!.type!!,
            priority = _uiState.value!!.priority!!,
            number = _uiState.value!!.numberText!!.toInt(),
            frequency = _uiState.value!!.frequencyText!!.toInt(),
            colorHabit = colorHabit,
            date = getTime(),
            isSentToServer = false,
            id = getId()
        )
    }

}