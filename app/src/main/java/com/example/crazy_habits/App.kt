package com.example.crazy_habits

import android.app.Application
import com.example.crazy_habits.utils.ContextSharedPrefs

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextSharedPrefs.createSharedPrefs(this)

    }
}