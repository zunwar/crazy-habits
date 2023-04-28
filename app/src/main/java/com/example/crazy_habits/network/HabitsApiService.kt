package com.example.crazy_habits.network

import com.example.crazy_habits.database.habit.DataOfHabit
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.database.habit.HabitUID
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


private const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
private const val API_TOKEN = "458fbcfe-179e-4e5c-9171-c457e060e2d6"

 private val moshi = Moshi.Builder()
    .add(HabitEntityAdapter())
    .add(DataOfHabitAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface HabitsApiService {
    @GET("habit")
    suspend fun getHabits(@Header("Authorization") token: String = API_TOKEN): NetworkResult<List<HabitEntity>>

    @PUT("habit")
    suspend fun putHabit(@Header("Authorization") token: String = API_TOKEN,  @Body habitData : DataOfHabit): NetworkResult<HabitUID>

    @PUT("habit")
    suspend fun changeHabit(@Header("Authorization") token: String = API_TOKEN,  @Body habit : HabitEntity): NetworkResult<HabitUID>

    @POST("habit")
    suspend fun postHabit(@Header("Authorization") token: String = API_TOKEN): List<HabitEntity>

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Header("Authorization") token: String = API_TOKEN, @Body uid: HabitUID): NetworkResult<Unit>

}

object HabitsApi {
    val retrofitService: HabitsApiService by lazy {
        retrofit.create(HabitsApiService::class.java)
    }
}
