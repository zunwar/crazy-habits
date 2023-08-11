package com.example.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.entities.Priority
import com.example.domain.entities.Type

@Entity
data class HabitEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "type") val type: Type,
    @ColumnInfo(name = "priority") val priority: Priority,
    @ColumnInfo(name = "number") val number: Int,
    @ColumnInfo(name = "frequency") val frequency: Int,
    @ColumnInfo(name = "color") val colorHabit: Int,
    @ColumnInfo(name = "isSentToServer") val isSentToServer: Boolean = false,
    @ColumnInfo(name = "date") val date: Int,
    @ColumnInfo(name = "doneCount") val doneCount: Int,
    @PrimaryKey val id: String
)

fun HabitEntity.toHabitDto(uid: String?): HabitDto = HabitDto(
    name = this.name,
    desc = this.desc,
    type = when (type) {
        Type.Good -> 0
        Type.Bad -> 1
    },
    priority = when (priority) {
        Priority.High -> 0
        Priority.Middle -> 1
        Priority.Low -> 2
    },
    number = this.number,
    frequency = this.frequency,
    colorHabit = this.colorHabit,
    date = this.date,
    uid = uid
)
