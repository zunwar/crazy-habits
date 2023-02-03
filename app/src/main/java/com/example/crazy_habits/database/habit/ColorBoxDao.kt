package com.example.crazy_habits.database.habit

import androidx.room.*
import com.example.crazy_habits.ColorBox
import kotlinx.coroutines.flow.Flow

@Dao
interface ColorBoxDao {

    @Query("SELECT * FROM ColorBoxEntity")
    fun getAllColorBoxes(): Flow<List<ColorBox>>

    @Query("SELECT * FROM ColorBoxEntity WHERE id IN (:boxId) LIMIT 1")
    fun getColorBoxById(boxId: String): Flow<ColorBoxEntity>

    @Update
    suspend fun update(vararg box: ColorBoxEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg box: ColorBoxEntity)
}