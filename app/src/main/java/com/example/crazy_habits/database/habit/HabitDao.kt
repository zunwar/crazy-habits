package com.example.crazy_habits.database.habit

import androidx.room.*
import com.example.crazy_habits.utils.Type
import kotlinx.coroutines.flow.Flow


@Dao
interface HabitDao {

    @Query("SELECT * FROM HabitEntity")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM HabitEntity WHERE id IN (:habitId) LIMIT 1")
    fun getHabitById(habitId: String): Flow<HabitEntity>

    @Query("SELECT * FROM HabitEntity WHERE type IN (:type)")
    fun getHabitsByType(type: Type): Flow<List<HabitEntity>>

    @Query("SELECT * FROM HabitEntity WHERE name IN (:habitName) LIMIT 1")
    fun getHabitsByName(habitName: String): Flow<List<HabitEntity>>

    @Query("SELECT * FROM HabitEntity WHERE name LIKE '%' || :name || '%' AND type IN (:type)")
    fun searchHabitsByNameAndType(name: String, type: Type): Flow<List<HabitEntity>>

    @Update
    suspend fun updateHabit(vararg habit: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg habit: HabitEntity)

    @Delete
    fun delete(habit: HabitEntity)

    @Query("DELETE FROM HabitEntity WHERE id IN (:id)")
    suspend fun deleteById(id: String)

}