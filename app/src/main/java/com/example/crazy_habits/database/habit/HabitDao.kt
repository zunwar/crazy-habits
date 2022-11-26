package com.example.crazy_habits.database.habit

import androidx.room.Dao
import androidx.room.Query

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAllHabits(): List<Habit>
}