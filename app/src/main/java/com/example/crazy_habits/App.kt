package com.example.crazy_habits

import android.app.Application
import com.example.crazy_habits.utils.ContextSharedPrefs
import com.example.crazy_habits.utils.FileStreams

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextSharedPrefs.createSharedPrefs(this)
        FileStreams.createFileStreams(this)

    }
}