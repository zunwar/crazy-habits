package com.example.crazy_habits.colorchoose

import com.example.crazy_habits.utils.ColorBox
import com.example.crazy_habits.database.habit.ColorBoxDao
import com.example.crazy_habits.database.habit.ColorBoxEntity
import kotlinx.coroutines.flow.Flow

class ColorModel(private val colorBoxDao: ColorBoxDao) {

    suspend fun add(cb: ColorBoxEntity) {
        colorBoxDao.insertAll(cb)
    }

    fun getColorBoxEntity(habitId: String): Flow<ColorBoxEntity> {
        return colorBoxDao.getColorBoxById(habitId)
    }

    fun getAllColorBoxes(): Flow<List<ColorBox>> {
        return colorBoxDao.getAllColorBoxes()
    }

}