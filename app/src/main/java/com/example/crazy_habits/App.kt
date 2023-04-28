package com.example.crazy_habits

import android.app.Application
import com.example.crazy_habits.colorchoose.ColorModel
import com.example.crazy_habits.database.AppDatabase
import com.example.crazy_habits.edithabits.HabitModel
import com.example.crazy_habits.network.HabitsApi
import com.example.crazy_habits.network.NetworkConnectivityObserver

class App : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDataBase(this) }
    val habitModel by lazy { HabitModel(database.habitDao(), HabitsApi.retrofitService, NetworkConnectivityObserver(this)) }
    val colorModel by lazy { ColorModel(database.colorBoxDao()) }
}