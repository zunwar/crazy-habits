package com.example.crazy_habits.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.crazy_habits.database.habit.ColorBoxDao
import com.example.crazy_habits.database.habit.ColorBoxEntity
import com.example.crazy_habits.database.habit.HabitDao
import com.example.crazy_habits.database.habit.HabitEntity

@Database(entities = [HabitEntity::class, ColorBoxEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun colorBoxDao(): ColorBoxDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDataBase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "app_database"
                    ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}