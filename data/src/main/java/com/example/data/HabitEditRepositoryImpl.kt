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
        var serverResponse = ""
        var habitToAdd = habit
        when (val response = retrofitService.putHabit(habitData = habit.toEntity().toHabitDto())) {
            is NetworkResult.Success -> {
                if (response.code == 200 && response.message == "OK") {
                    habitToAdd = habit.copy(isSentToServer = true, id = response.data.uid)
                    serverResponse = "${response.code} ${response.message}"
                }
            }
            is NetworkResult.Error -> {
                serverResponse =
                    "\n${response.responseError!!.code}\n${response.responseError.message}"
            }
            is NetworkResult.Exception -> serverResponse = "${response.e.message}"
        }
        habitDao.insertAll(habitToAdd.toEntity())
        return serverResponse
    }


    override suspend fun changeHabit(habit: Habit): String {
        var habitToChange = habit
        var serverResponse = ""
        when (val response = retrofitService.changeHabit(habit = habit.toEntity())) {
            is NetworkResult.Success -> {
                if (response.code == 200 && response.message == "OK") {
                    habitToChange = habit.copy(isSentToServer = true)
                    serverResponse = "${response.code} ${response.message}"
                }
            }
            is NetworkResult.Error -> {
                serverResponse =
                    "\n${response.responseError!!.code}\n${response.responseError.message}"
            }
            is NetworkResult.Exception -> {
                serverResponse = "${response.e.message}"
            }
        }
        habitDao.updateHabit(habitToChange.toEntity())
        return serverResponse
    }

    override suspend fun getHabitToEdit(idHabit: String): Flow<Habit> =
        habitDao.getHabitById(idHabit).map {
            it.toDomain()
        }

}