package com.example.crazy_habits.models

import com.example.crazy_habits.utils.FileStreams

class ColorModel {

    private val fileStr = FileStreams.getFileStreams()
    private var _listPartHabit : MutableList<String> = mutableListOf()
    private val listFileName =  "ColorList"

    fun saveListToFile(list: List<String>) {
        _listPartHabit = getListFromFile()
        if (_listPartHabit.contains(list[0])){
            val idIndex = _listPartHabit.indexOf(list[0])
            _listPartHabit[idIndex+1] = list[1]
            _listPartHabit[idIndex+2] = list[2]
            fileStr.saveListToFile(listFileName, _listPartHabit)
        } else {
            if (_listPartHabit.isEmpty() || _listPartHabit[0] == "") {
                _listPartHabit = mutableListOf(list[0], list[1], list[2])
            }
            else {
                _listPartHabit.addAll(list)
            }
            fileStr.saveListToFile(listFileName, _listPartHabit)
        }
    }

    fun getListFromFile () : MutableList<String> {
        return fileStr.getListFromFile(listFileName).toMutableList()
    }

    fun isNew(idHabit: String): Boolean {
        _listPartHabit = getListFromFile()
       return !_listPartHabit.contains(idHabit)
    }

}