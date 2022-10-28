package com.example.crazy_habits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crazy_habits.models.ColorModel

class ColorViewModel : ViewModel() {
    private val colorModel = ColorModel()

    private val _listPartHabit: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    val listPartHabit : LiveData<List<String>> = _listPartHabit
    private var idHabit : String = ""

    init{
        load()
    }

    private fun load() {
        _listPartHabit.postValue(getList())
    }

    fun saveData(list: List<String>) {
        colorModel.saveListToFile(list)
        idHabit = list[0]
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
        idHabit = id
        return colorModel.isNew(id)
    }


}