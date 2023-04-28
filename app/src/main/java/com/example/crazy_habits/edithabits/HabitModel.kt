package com.example.crazy_habits.edithabits

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.database.habit.*
import com.example.crazy_habits.network.ConnectivityObserver
import com.example.crazy_habits.utils.Type
import com.example.crazy_habits.network.HabitsApiService
import com.example.crazy_habits.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.*

class HabitModel(
    private val habitDao: HabitDao,
    private val retrofitService: HabitsApiService,
    private val connectivityObserver: ConnectivityObserver
) {

    init {
        Log.d(TAG, "HabitModel created")
    }

/*
* при появлении сети формируется список неотправленных привычек нужного типа
* пребразуются в тип DataOfHabit
* постепенно каждая привычка пробуется отправить на сервер и получить id,
* если id не будет получет, то цикл(отправка) прервутся и в базу пойдет запись того, что есть
* при записи локальный id привычки перезаписывается на id полученный от сервера
* */
    suspend fun syncHabitsWithServer(type: Type){
        connectivityObserver.observe().collect{ networkStatus ->
            when (networkStatus){
                ConnectivityObserver.NetworkStatus.Available -> {
                    val listUnsentHabitEntity = habitDao.getUnsentHabits(isSentToServer = isSentToServerFalse, type) as MutableList<HabitEntity>
                    val listUnsentDataOfHabit: MutableList<DataOfHabit> = mutableListOf()
                    listUnsentHabitEntity.forEach {
                        listUnsentDataOfHabit.add(
                            fromHabitEntityToDataOfHabit(it)
                        )
                    }
                    val listIdFromServer: MutableList<String> = mutableListOf()
                    listUnsentDataOfHabit.forEach {
                        val id = addHabitTrySync(it)
                        if(id.isNotEmpty()){
                            listIdFromServer.add(id)
                        }else return@forEach
                    }
                    val last = listIdFromServer.lastIndex
                    for (n in 0..last) {habitDao.updateIdWithIdNetwork(listUnsentHabitEntity[n].id, listIdFromServer[n], isSentToServer = isSentToServerTrue)}
                }
                ConnectivityObserver.NetworkStatus.Unavailable -> {}
                ConnectivityObserver.NetworkStatus.Losing      -> {}
                ConnectivityObserver.NetworkStatus.Lost        -> {}
            }
        }
    }

    private suspend fun addHabitTrySync(dataOfHabit: DataOfHabit): String {
        var id = ""
        when (val response = retrofitService.putHabit(habitData = dataOfHabit)) {
            is NetworkResult.Success -> {
                if (response.code == 200 && response.message == "OK") {
                    id = response.data.uid
                }
            }
            is NetworkResult.Error -> {
                Log.d(TAG, "HabitModel---addHabitTrySync ${response.responseError}")
            }
            is NetworkResult.Exception -> {
                Log.d(TAG, "HabitModel---addHabitTrySync ${response.e.message}")
            }
        }
        return id
    }

    suspend fun addHabit(dataOfHabit: DataOfHabit): String {
        var serverResponse = ""
        when (val response = retrofitService.putHabit(habitData = dataOfHabit)) {
            is NetworkResult.Success -> {
                if (response.code == 200 && response.message == "OK") {
                    val habitToSave = fromDataOfHabitToHabitEntity(
                        dataOfHabit = dataOfHabit,
                        id = response.data.uid,
                        isSentToServer = true
                    )
                    habitDao.insertAll(habitToSave)
                    serverResponse = "${response.code} ${response.message}"
                }
            }
            is NetworkResult.Error -> {
//все равно сохраняем привычку в базу, но с локальным id, ставим флаг isSentToServer = false
                val habitToSave = fromDataOfHabitToHabitEntity(
                    dataOfHabit = dataOfHabit,
                    id = UUID.randomUUID().toString(),
                    isSentToServer = false,
                )
                habitDao.insertAll(habitToSave)
                serverResponse = "\n${response.responseError!!.code}\n${response.responseError.message}"
            }
            is NetworkResult.Exception -> {
//все равно сохраняем привычку в базу, но с локальным id, ставим флаг isSentToServer = false
                val habitToSave = fromDataOfHabitToHabitEntity(
                    dataOfHabit = dataOfHabit,
                    id = UUID.randomUUID().toString(),
                    isSentToServer = false,
                )
                habitDao.insertAll(habitToSave)
                serverResponse = "${response.e.message}"
            }
        }
        return serverResponse
    }

    fun getHabitsByType(type: Type): Flow<List<HabitEntity>> {
        return habitDao.getHabitsByType(type)
    }

    suspend fun changeHabit(habit: HabitEntity): String {
        var serverResponse = ""
        when (val response = retrofitService.changeHabit(habit = habit)) {
            is NetworkResult.Success -> {
                if (response.code == 200 && response.message == "OK") {
                    habitDao.updateHabit(habit)
                    serverResponse = "${response.code} ${response.message}"
                }
            }
            is NetworkResult.Error -> {
//все равно сохраняем изменения в базу, ставим флаг isSentToServer = false
                val habitToSave = habit.copy(isSentToServer = false)
                habitDao.updateHabit(habitToSave)
                serverResponse =
                    "\n${response.responseError!!.code}\n${response.responseError.message}"
            }
            is NetworkResult.Exception -> {
//все равно сохраняем изменения в базу, ставим флаг isSentToServer = false
                val habitToSave = habit.copy(isSentToServer = false)
                habitDao.updateHabit(habitToSave)
                serverResponse = "${response.e.message}"
            }
        }
        return serverResponse
    }

    fun getHabitToEdit(id: String): Flow<HabitEntity> {
        return habitDao.getHabitById(id)
    }

    fun getHabitsByNameAndType(name: String, type: Type): Flow<List<HabitEntity>> {
        return habitDao.searchHabitsByNameAndType(name, type)
    }

    fun getHabitsByNameAndTypeAndSortASC(name: String, type: Type): Flow<List<HabitEntity>> {
        return habitDao.getHabitsByNameAndTypeAndSort(name, type, sortAscending)
    }

    fun getHabitsByNameAndTypeAndSortDESC(name: String, type: Type): Flow<List<HabitEntity>> {
        return habitDao.getHabitsByNameAndTypeAndSort(name, type, sortDescending)
    }

    suspend fun getHabitsFromServer() {
        withContext(Dispatchers.IO) {
            when (val response = retrofitService.getHabits()) {
                is NetworkResult.Success -> {
                    val list: List<HabitEntity> = response.data
                    list.forEach { habitDao.insertAll(it) }
                }
                is NetworkResult.Error -> Log.d(
                    TAG, "HabitModel---getHabitsFromServer Error ${response.responseError}"
                )
                is NetworkResult.Exception -> Log.d(
                    TAG, "HabitModel---getHabitsFromServer Exception ${response.e}"
                )
            }
        }
    }

    suspend fun deleteHabitById(id: String) {
        habitDao.deleteById(id)
        val uid = HabitUID(id)
        when (val response = retrofitService.deleteHabit(uid = uid)) {
            is NetworkResult.Success -> Log.d(
                TAG,
                "HabitModel---deleteHabitById Success ${response.message}---${response.code}"
            )
            is NetworkResult.Error -> Log.d(
                TAG,
                "HabitModel---deleteHabitById Error ${response.responseError}"
            )
            is NetworkResult.Exception -> Log.d(
                TAG,
                "HabitModel---deleteHabitById Exception ${response.e}"
            )
        }
    }

    suspend fun getUriToAvatarDownload(): Flow<Uri> = flow {
        val imgUrl = "https://i.imgur.com/bMhYfSR.png "
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        emit(imgUri)
        connectivityObserver.observe().collect { emit(imgUri) }
    }

    private fun fromHabitEntityToDataOfHabit(habit: HabitEntity): DataOfHabit {
        return DataOfHabit(
            name = habit.name,
            desc = habit.desc,
            type = habit.type,
            priority = habit.priority,
            number = habit.number,
            frequency = habit.frequency,
            colorHabit = habit.colorHabit,
            date = habit.date
        )
    }

    private fun fromDataOfHabitToHabitEntity(
        dataOfHabit: DataOfHabit,
        id: String,
        isSentToServer: Boolean
    ): HabitEntity {
        return HabitEntity(
            name = dataOfHabit.name,
            desc = dataOfHabit.desc,
            type = dataOfHabit.type,
            priority = dataOfHabit.priority,
            number = dataOfHabit.number,
            frequency = dataOfHabit.frequency,
            colorHabit = dataOfHabit.colorHabit,
            date = dataOfHabit.date,
            isSentToServer = isSentToServer,
            id = id
        )
    }

    companion object {
        private const val sortAscending = 1
        private const val sortDescending = 2
        private const val isSentToServerFalse = 0
        private const val isSentToServerTrue = 1
    }
}