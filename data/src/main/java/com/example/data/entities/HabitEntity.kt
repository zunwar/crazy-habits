package com.example.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import com.squareup.moshi.Json

@Entity
data class HabitEntity(
    @Json(name = "title") @ColumnInfo(name = "name") val name: String,
    @Json(name = "description") @ColumnInfo(name = "desc") val desc: String,
    @Json(name = "type") @ColumnInfo(name = "type") val type: Type,
    @Json(name = "priority") @ColumnInfo(name = "priority") val priority: Priority,
    @Json(name = "count") @ColumnInfo(name = "number") val number: Int,
    @Json(name = "frequency") @ColumnInfo(name = "frequency") val frequency: Int,
    @Json(name = "color") @ColumnInfo(name = "color") val colorHabit: Int,
    @ColumnInfo(name = "isSentToServer") val isSentToServer: Boolean = false,
    @Json(name = "date") @ColumnInfo(name = "date") val date: Int,
    @ColumnInfo(name = "doneCount") val doneCount: Int,
    @Json(name = "uid") @PrimaryKey val id: String
)

fun HabitEntity.toHabitDto(): HabitDto = HabitDto(
    name = this.name,
    desc = this.desc,
    type = this.type,
    priority = this.priority,
    number = this.number,
    frequency = this.frequency,
    colorHabit = this.colorHabit,
    date = this.date
)
