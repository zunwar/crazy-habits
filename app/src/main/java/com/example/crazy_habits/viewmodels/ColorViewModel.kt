package com.example.crazy_habits.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.Habit
import com.example.crazy_habits.fragments.ColorHabitFragment
import com.example.crazy_habits.fragments.HabitEditFragment
import com.example.crazy_habits.models.ColorModel

class ColorViewModel(private val idHabit: String) : ViewModel() {
    private val colorModel = ColorModel()

    private val _listPartHabit: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    val listPartHabit : LiveData<List<String>> = _listPartHabit

    init{
        load()
        Log.d(TAG, "initColorViewModel: $idHabit")
    }

    private fun load() {
        _listPartHabit.postValue(getList())
    }

    fun saveData(list: List<String>) {
        colorModel.saveListToFile(list)
        load()
    }

    private fun getList(): List<String> {
        return colorModel.getListFromFile()
    }

    fun getNumberOfPressedBox(): Int {
        val list = getList()
        val idIndex = list.indexOf(idHabit)
        return list[idIndex + 2].toInt()
    }

    fun getColorOfPressedBox() : Int {
        colorModel.getListFromFile()
        val list = getList()
        val idIndex = list.indexOf(idHabit)
        return list[idIndex + 1].toInt()
    }

    fun isNew(id: String): Boolean {
        return colorModel.isNew(id)
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val frag = checkNotNull(extras[VIEW_MODEL_STORE_OWNER_KEY])
                return ColorViewModel(
                    (frag as ColorHabitFragment).requireArguments().getParcelable<Habit>(HabitEditFragment.COLLECTED_HABIT)!!.id
                ) as T
            }
        }
    }



}