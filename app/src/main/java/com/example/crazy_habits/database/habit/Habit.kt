package com.example.crazy_habits.database.habit

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crazy_habits.Priority
import com.example.crazy_habits.Type
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Habit(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "type") val type: Type,
    @ColumnInfo(name = "priority") val priority: Priority,
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "period") val period: String,
    @ColumnInfo(name = "color") val colorHabit: Int,
    @PrimaryKey val id: String
) : Parcelable
