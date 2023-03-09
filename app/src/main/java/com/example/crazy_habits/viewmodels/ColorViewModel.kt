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
import com.example.crazy_habits.fragments.ColorHabitFragment
import com.example.crazy_habits.fragments.HabitEditFragment
import com.example.crazy_habits.models.ColorModel
import kotlinx.coroutines.launch

class ColorViewModel(colorBoxDao: ColorBoxDao, private val id: String) : ViewModel() {
    private val model = ColorModel(colorBoxDao)
    private var _closeColorFragment = SingleLiveEvent<Boolean>()
    val closeColorFragment = _closeColorFragment
    private var _colorBoxEntity: MutableLiveData<ColorBoxEntity> = MutableLiveData()
    val colorBoxEntity: MutableLiveData<ColorBoxEntity> = _colorBoxEntity

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
            model.getAllColorBoxes().collect { list ->
                if ((list.find { it.id == id }) == null) {
                    addNewColorBox()
                } else {
                    getColorBox()
                }
            }
        }
    }

    private suspend fun getColorBox() {
        model.getColorBoxEntity(id).collect {
            _colorBoxEntity.postValue(it)
        }
    }

    private suspend fun save(cbe: ColorBoxEntity) {
        model.add(cbe)
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

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val app =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val frag = checkNotNull(extras[VIEW_MODEL_STORE_OWNER_KEY])
                return ColorViewModel(
                    (app as App).database.colorBoxDao(),
                    (frag as ColorHabitFragment).requireArguments().let {
                        it.getString(HabitEditFragment.COLLECTED_HABIT)!!
                    }
                ) as T
            }
        }
    }

}