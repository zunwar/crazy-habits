package com.example.domain.repository

import android.net.Uri
import com.example.domain.entities.Habit
import com.example.domain.entities.Type
import kotlinx.coroutines.flow.Flow


interface HabitListRepository {

    suspend fun getHabitsByNameAndTypeAndSortASC(nameToFilter: String, type: Type): Flow<List<Habit>>

    suspend fun getHabitsByNameAndTypeAndSortDESC(nameToFilter: String, type: Type): Flow<List<Habit>>

    suspend fun getHabitsByNameAndType(nameToFilter: String, type: Type): Flow<List<Habit>>

    suspend fun deleteHabit(idHabit: String): String

    suspend fun syncHabitsWithServer(type: Type)

    suspend fun getAvatar(): Flow<Uri>

    suspend fun changeHabit(habit: Habit)

}