package com.example.crazy_habits.database.habit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HabitDao {
    @Query("SELECT * FROM HabitEntity")
    fun getAllHabits(): List<HabitEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(habit: List<HabitEntity>)

}