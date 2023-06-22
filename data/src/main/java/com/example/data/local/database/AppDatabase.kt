package com.example.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.entities.ColorBoxEntity
import com.example.data.entities.HabitEntity

@Database(entities = [HabitEntity::class, ColorBoxEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun colorBoxDao(): ColorBoxDao

}