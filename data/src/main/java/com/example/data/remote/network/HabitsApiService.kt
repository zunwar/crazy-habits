package com.example.data.remote.network

import com.example.data.entities.HabitDto
import com.example.data.entities.HabitEntity
import retrofit2.http.*

private const val API_TOKEN = "458fbcfe-179e-4e5c-9171-c457e060e2d6"

interface HabitsApiService {
    @GET("habit")
    suspend fun getHabits(@Header("Authorization") token: String = API_TOKEN): NetworkResult<List<HabitEntity>>

    @PUT("habit")
    suspend fun putHabit(@Header("Authorization") token: String = API_TOKEN, @Body habitData : HabitDto): NetworkResult<HabitUID>

    @PUT("habit")
    suspend fun changeHabit(@Header("Authorization") token: String = API_TOKEN, @Body habit : HabitEntity): NetworkResult<HabitUID>

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Header("Authorization") token: String = API_TOKEN, @Body uid: HabitUID): NetworkResult<Unit>

}