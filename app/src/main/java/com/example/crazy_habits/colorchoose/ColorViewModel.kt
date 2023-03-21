package com.example.crazy_habits.colorchoose

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.crazy_habits.App
import com.example.crazy_habits.utils.ColorBoxNum
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.SingleLiveEvent
import com.example.crazy_habits.database.habit.ColorBoxEntity
import kotlinx.coroutines.launch

class ColorViewModel(private val colorModel: ColorModel, private val id: String) : ViewModel() {
    private var _closeColorFragment = SingleLiveEvent<Boolean>()
    val closeColorFragment = _closeColorFragment
    private var _colorBoxEntity: MutableLiveData<ColorBoxEntity> = MutableLiveData()
    val colorBoxEntity: MutableLiveData<ColorBoxEntity> = _colorBoxEntity
    private val _isDoneCreatingBoxes: MutableLiveData<Boolean> = MutableLiveData()
    val isDoneCreatingBoxes = _isDoneCreatingBoxes

    init {
        Log.d(TAG, "initColorViewModel")
    }

    private suspend fun addNewColorBox() {
        val cbe = ColorBoxEntity(
            id,
            -1,
            ColorBoxNum.One
        )
        save(cbe)
    }

    fun saveIfNew() {
        viewModelScope.launch {
            colorModel.getAllColorBoxes().collect { list ->
                if ((list.find { it.id == id }) == null) {
                    addNewColorBox()
                } else {
                    getColorBox()
                }
            }
        }
    }

    private suspend fun getColorBox() {
        colorModel.getColorBoxEntity(id).collect {
            _colorBoxEntity.postValue(it)
        }
    }

    private suspend fun save(cbe: ColorBoxEntity) {
        colorModel.add(cbe)
    }

    fun saveSelectedColorAndNum(color: Int, num: Int) {
        viewModelScope.launch {
            val cbe = ColorBoxEntity(
                id = id,
                color = color,
                colorBoxNum = ColorBoxNum.values()[num]
            )
            save(cbe)
        }
    }

    fun closeColorFragment() {
        _closeColorFragment.postValue(true)
    }

    fun isDoneCreatingBoxes(isDone: Boolean) {
        _isDoneCreatingBoxes.postValue(isDone)
    }

    companion object {
        fun provideFactory(
            id: String
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    val app = checkNotNull(extras[APPLICATION_KEY])
                    return ColorViewModel(
                        (app as App).colorModel,
                        id
                    ) as T
                }
            }
    }

}