package com.example.data

import com.example.data.entities.toHabitDto
import com.example.data.local.database.HabitDao
import com.example.data.mappers.toDomain
import com.example.data.mappers.toEntity
import com.example.data.remote.network.HabitsApiService
import com.example.data.remote.network.NetworkResult
import com.example.domain.entities.Habit
import com.example.domain.repository.HabitEditRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class HabitEditRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val retrofitService: HabitsApiService
) :
    HabitEditRepository {

    /*
    * отправка привычки на сервер, чтобы сохранить на сервере и получить от сервера id привычки,
    * привычку сохряем в БД, в случае ошибки сохранение в БД идет
    * с локальным id и флагом isSentToServer = false
    */
    override suspend fun addHabit(habit: Habit): String {
        val serverResponse = when (val response =
            retrofitService.putHabit(habitDto = habit.toEntity().toHabitDto(uid = null))) {
            is NetworkResult.Success -> {
                val habitToAdd = habit.copy(isSentToServer = true, id = response.data.uid)
                habitDao.insertAll(habitToAdd.toEntity())
                "${response.code} ${response.message}"
            }
            is NetworkResult.Error -> {
                val habitToAdd = habit.copy(id = UUID.randomUUID().toString())
                habitDao.insertAll(habitToAdd.toEntity())
                "\n${response.responseError!!.code}\n${response.responseError.message}"
            }
            is NetworkResult.Exception -> {
                val habitToAdd = habit.copy(id = UUID.randomUUID().toString())
                habitDao.insertAll(habitToAdd.toEntity())
                "${response.e.message}"
            }
        }
        return serverResponse
    }

    /*
    * изменение привычки, в любом случае сохраняем изменения в БД,
    * при появлении сети данные будут синхронизированны
    */
    override suspend fun changeHabit(habit: Habit): String {
        val serverResponse =
            when (val response = retrofitService.changeHabit(habitDto = habit.toEntity().toHabitDto(uid = habit.id))) {
                is NetworkResult.Success -> {
                    val habitToChange = habit.copy(isSentToServer = true)
                    habitDao.updateHabit(habitToChange.toEntity())
                    "${response.code} ${response.message}"
                }
                is NetworkResult.Error -> {
                    habitDao.updateHabit(habit.toEntity())
                    "\n${response.responseError!!.code}\n${response.responseError.message}"
                }
                is NetworkResult.Exception -> {
                    habitDao.updateHabit(habit.toEntity())
                    "${response.e.message}"
                }
            }
        return serverResponse
    }

    override suspend fun getHabitToEdit(idHabit: String): Flow<Habit> =
        habitDao.getHabitById(idHabit).map {
            it.toDomain()
        }

}