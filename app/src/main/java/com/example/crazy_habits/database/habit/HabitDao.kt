package com.example.crazy_habits.database.habit

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.crazy_habits.Type

@Dao
interface HabitDao {
    @Query("SELECT * FROM HabitEntity")
    fun getAllHabits(): LiveData<List<HabitEntity>>

    @Query("SELECT * FROM HabitEntity WHERE id IN (:habitId) LIMIT 1")
    fun getHabitById(habitId: String): LiveData<HabitEntity>

    @Query("SELECT * FROM HabitEntity WHERE type IN (:type)")
    fun getHabitsByType(type: Type): LiveData<List<HabitEntity>>

    @Query("SELECT * FROM HabitEntity WHERE name IN (:habitName) LIMIT 1")
    fun getHabitsByName(habitName: String): LiveData<List<HabitEntity>>

    @Update
    fun updateHabit(vararg habit: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg habit: HabitEntity)

    @Delete
    fun delete(habit: HabitEntity)
}