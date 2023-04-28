package com.example.crazy_habits.database.habit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crazy_habits.utils.Priority
import com.example.crazy_habits.utils.Type
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
    @Json(name = "uid") @PrimaryKey val id: String
)

@JvmInline
value class NameToFilter(val string: String)
val NoName = NameToFilter("")

data class DataOfHabit(
    @Json(name = "title") val name: String,
    @Json(name = "description") val desc: String,
    @Json(name = "type") val type: Type,
    @Json(name = "priority") val priority: Priority,
    @Json(name = "count") val number: Int,
    @Json(name = "frequency") val frequency: Int,
    @Json(name = "color") val colorHabit: Int,
    @Json(name = "date") val date: Int,
    @Json(name = "done_dates") val done_dates: Int = 0,
)

data class ResponseError(
    @Json(name = "code") val code: Int?,
    @Json(name = "message") val message: String?,
)

data class HabitUID(
    @Json(name = "uid") val uid: String,
)
