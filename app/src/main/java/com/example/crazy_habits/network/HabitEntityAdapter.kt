package com.example.crazy_habits.network

import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.utils.Priority
import com.example.crazy_habits.utils.Type
import com.squareup.moshi.*

class HabitEntityAdapter : JsonAdapter<HabitEntity>() {
    @FromJson
    override fun fromJson(reader: JsonReader): HabitEntity {
        reader.beginObject()

        var name: String? = null
        var desc: String? = null
        var type: Type? = null
        var priority: Priority? = null
        var number = 0
        var frequency = 0
        var date = 0
        var colorHabit: Int? = null
        var id: String? = null

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "title" -> name = reader.nextString()
                "description" -> desc = reader.nextString()
                "type" -> type = when (reader.nextInt()) {
                    0 -> Type.Good
                    1 -> Type.Bad
                    else -> null
                }
                "priority" -> priority = when (reader.nextInt()) {
                    0 -> Priority.High
                    1 -> Priority.Middle
                    2 -> Priority.Low
                    else -> null
                }
                "count" -> number = reader.nextInt()
                "frequency" -> frequency = reader.nextInt()
                "date" -> date = reader.nextInt()
                "color" -> colorHabit = reader.nextInt()
                "uid" -> id = reader.nextString()
                else -> reader.skipValue()
            }
        }

        reader.endObject()

        return HabitEntity(
            name = name.orEmpty(),
            desc = desc.orEmpty(),
            type = type ?: Type.Good,
            priority = priority ?: Priority.Low,
            number = number,
            frequency = frequency,
            date = date,
            colorHabit = colorHabit ?: 0,
            isSentToServer = true,
            id = id.orEmpty()
        )
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: HabitEntity?) {
        value?.let {
            writer.beginObject()
            writer.name("title").value(it.name)
            writer.name("description").value(it.desc)
            writer.name("type").value(
                when (it.type) {
                    Type.Good -> 0
                    Type.Bad -> 1
                }
            )
            writer.name("priority").value(
                when (it.priority) {
                    Priority.High -> 0
                    Priority.Middle -> 1
                    Priority.Low -> 2
                }
            )
            writer.name("count").value(it.number)
            writer.name("frequency").value(it.frequency)
            writer.name("date").value(it.date)
            writer.name("color").value(it.colorHabit)
            writer.name("uid").value(it.id)
            writer.endObject()
        } ?: writer.nullValue()
    }
}