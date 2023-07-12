package com.example.data

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.data.entities.HabitDto
import com.example.data.entities.HabitEntity
import com.example.data.entities.toHabitDto
import com.example.data.local.database.HabitDao
import com.example.data.mappers.toDomain
import com.example.data.mappers.toEntity
import com.example.data.remote.network.ConnectivityObserver
import com.example.data.remote.network.HabitUID
import com.example.data.remote.network.HabitsApiService
import com.example.data.remote.network.NetworkResult
import com.example.domain.Constants.TAG
import com.example.domain.entities.Habit
import com.example.domain.entities.Type
import com.example.domain.repository.HabitListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitListRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val retrofitService: HabitsApiService,
    private val connectivityObserver: ConnectivityObserver
): HabitListRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getHabitsFromServer()
        }
    }

    private suspend fun getHabitsFromServer() {
        when (val response = retrofitService.getHabits()) {
            is NetworkResult.Success -> {
                val list: List<HabitEntity> = response.data
                list.forEach { habitDao.insertAll(it) }
            }
            is NetworkResult.Error -> Log.d(
                TAG, "getHabitsFromServer Error ${response.responseError}"
            )
            is NetworkResult.Exception -> Log.d(
                TAG, "getHabitsFromServer Exception ${response.e}"
            )
        }
    }

    override suspend fun getHabitsByNameAndTypeAndSortASC(
        nameToFilter: String,
        type: Type
    ): Flow<List<Habit>> {
        return habitDao.getHabitsByNameAndTypeAndSort(nameToFilter, type, sortAscending).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getHabitsByNameAndTypeAndSortDESC(
        nameToFilter: String,
        type: Type
    ): Flow<List<Habit>> {
        return habitDao.getHabitsByNameAndTypeAndSort(nameToFilter, type, sortDescending).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getHabitsByNameAndType(
        nameToFilter: String,
        type: Type
    ): Flow<List<Habit>> {
        return habitDao.searchHabitsByNameAndType(nameToFilter, type).map {list->
            list.map { it.toDomain() }
        }
    }

    override suspend fun deleteHabit(idHabit: String) {
        val uid = HabitUID(idHabit)
        when (val response = retrofitService.deleteHabit(uid = uid)) {
            is NetworkResult.Success -> {
                habitDao.deleteById(idHabit)
                Log.d(
                    TAG,
                    "deleteHabitById Success ${response.message}---${response.code}"
                )
            }
            is NetworkResult.Error -> Log.d(
                TAG,
                "deleteHabitById Error ${response.responseError}"
            )
            is NetworkResult.Exception -> Log.d(
                TAG,
                "deleteHabitById Exception ${response.e}"
            )
        }
    }

    /*
    * при появлении сети формируется список неотправленных привычек нужного типа
    * постепенно каждая привычка пробуется отправить на сервер и получить id,
    * если id не будет получен, то цикл(отправка) прервутся и в базу пойдет запись того, что есть
    * при записи локальный id привычки перезаписывается на id полученный от сервера
    */
    override suspend fun syncHabitsWithServer(type: Type) {
        connectivityObserver.observe().collect { networkStatus ->
            when (networkStatus) {
                ConnectivityObserver.NetworkStatus.Available -> {
                    val listUnsentHabit = habitDao.getUnsentHabits(
                        isSentToServer = isSentToServerFalse,
                        type
                    ) as MutableList<HabitEntity>
                    val listUnsentHabitDto = listUnsentHabit.map { it.toHabitDto() }
                    val listIdFromServer: MutableList<String> = mutableListOf()
                    listUnsentHabitDto.forEach {
                        val id = addHabitTrySync(it)
                        if (id.isNotEmpty()) {
                            listIdFromServer.add(id)
                        } else return@forEach
                    }
                    val last = listIdFromServer.lastIndex
                    for (n in 0..last) {
                        habitDao.updateIdWithIdNetwork(
                            listUnsentHabit[n].id,
                            listIdFromServer[n],
                            isSentToServer = isSentToServerTrue
                        )
                    }
                }
                ConnectivityObserver.NetworkStatus.Unavailable -> {}
                ConnectivityObserver.NetworkStatus.Losing -> {}
                ConnectivityObserver.NetworkStatus.Lost -> {}
            }
        }
    }

        private suspend fun addHabitTrySync(habitDto: HabitDto): String {
            var id = ""
            when (val response = retrofitService.putHabit(habitData = habitDto)) {
                is NetworkResult.Success -> {
                    if (response.code == 200 && response.message == "OK") {
                        id = response.data.uid
                    }
                }
                is NetworkResult.Error -> {
                    Log.d(TAG, "addHabitTrySync ${response.responseError}")
                }
                is NetworkResult.Exception -> {
                    Log.d(TAG, "addHabitTrySync ${response.e.message}")
                }
            }
            return id
        }

    override suspend fun getAvatar(): Flow<Uri> = flow {
        val imgUrl = "https://i.imgur.com/bMhYfSR.png"
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        emit(imgUri)
        connectivityObserver.observe().collect { emit(imgUri) }
    }

    override suspend fun changeHabit(habit: Habit) {
        habitDao.updateHabit(habit.toEntity())
    }

    companion object {
        private const val sortAscending = 1
        private const val sortDescending = 2
        private const val isSentToServerFalse = 0
        private const val isSentToServerTrue = 1
    }
}