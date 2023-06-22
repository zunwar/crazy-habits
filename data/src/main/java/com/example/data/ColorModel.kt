package com.example.data

import com.example.data.entities.ColorBoxEntity
import com.example.data.local.database.ColorBoxDao
import kotlinx.coroutines.flow.Flow

class ColorModel(private val colorBoxDao: ColorBoxDao) {

    suspend fun add(cb: ColorBoxEntity) {
        colorBoxDao.insertAll(cb)
    }

    fun getColorBoxEntity(habitId: String): Flow<ColorBoxEntity> {
        return colorBoxDao.getColorBoxById(habitId)
    }

    fun getAllColorBoxes(): Flow<List<com.example.domain.entities.ColorBox>> {
        return colorBoxDao.getAllColorBoxes()
    }

}