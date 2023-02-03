package com.example.crazy_habits.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.crazy_habits.App
import com.example.crazy_habits.ColorBoxNum
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.SingleLiveEvent
import com.example.crazy_habits.database.habit.ColorBoxDao
import com.example.crazy_habits.database.habit.ColorBoxEntity
import com.example.crazy_habits.models.ColorModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ColorViewModel(colorBoxDao: ColorBoxDao) : ViewModel() {
    private val model = ColorModel(colorBoxDao)
    private var _closeColorFragment = SingleLiveEvent<Boolean>()
    val closeColorFragment = _closeColorFragment
    private var id = ""
    private val _colorBoxViewList: MutableList<View> = mutableListOf()
    val colorBoxViewList: List<View> = _colorBoxViewList

    init {
        Log.d(TAG, "initColorViewModel")
    }

    fun setId(habitId: String) {
        id = habitId
    }

    private fun addNewColorBox(isNew: Boolean) {
        if (isNew) {
            val cbe = ColorBoxEntity(
                id,
                -1,
                ColorBoxNum.One
            )
            save(cbe)
        }
    }

    fun saveIfNew() {
        viewModelScope.launch {
            model.getAllColorBoxes().collect { list ->
                val isNew = (list.find { it.id == id }) == null
                addNewColorBox(isNew)
            }
        }
    }

    fun getColorBox(): Flow<ColorBoxEntity> {
        return model.getColorBoxEntity(id)
    }

    fun holdColorBoxesCoor(colorBoxView: View) {
        _colorBoxViewList.add(colorBoxView)
    }

    private fun save(cbe: ColorBoxEntity) {
        viewModelScope.launch {
            model.add(cbe)
        }
    }

    fun saveSelectedColorAndNum(color: Int, num: Int) {
        val cbe = ColorBoxEntity(
            id = id,
            color = color,
            colorBoxNum = ColorBoxNum.values()[num]
        )
        save(cbe)
    }

    fun closeColorFragment() {
        _closeColorFragment.postValue(true)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val app =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return ColorViewModel(
                    (app as App).database.colorBoxDao()
                ) as T
            }
        }
    }

}