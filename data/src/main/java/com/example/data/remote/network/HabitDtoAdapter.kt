package com.example.data.remote.network

import com.example.data.entities.HabitDto
import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import com.squareup.moshi.*

class HabitDtoAdapter : JsonAdapter<HabitDto>() {
    @FromJson
    override fun fromJson(reader: JsonReader): HabitDto {
        reader.beginObject()

        var name: String? = null
        var desc: String? = null
        var type: Type? = null
        var priority: Priority? = null
        var number = 0
        var frequency = 0
        var colorHabit: Int? = null
        var date: Int? = null
        var doneDates = 0

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
                "color" -> colorHabit = reader.nextInt()
                "date" -> date = reader.nextInt()
                "done_dates" -> doneDates = reader.nextInt()
                else -> reader.skipValue()
            }
        }

        reader.endObject()

        return HabitDto(
            name = name.orEmpty(),
            desc = desc.orEmpty(),
            type = type
                ?: Type.Good,
            priority = priority
                ?: Priority.Low,
            number = number,
            frequency = frequency,
            colorHabit = colorHabit
                ?: 0,
            date = date ?: 0,
            done_dates = doneDates
        )
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: HabitDto?) {
        value?.let {
            writer.beginObject()
            writer.name("color").value(it.colorHabit)
            writer.name("count").value(it.number)
            writer.name("date").value(it.date)
            writer.name("description").value(it.desc)
            writer.name("done_dates").beginArray().value(it.done_dates).endArray()
            writer.name("frequency").value(it.frequency)
            writer.name("priority").value(
                when (it.priority) {
                    Priority.High -> 0
                    Priority.Middle -> 1
                    Priority.Low -> 2
                }
            )
            writer.name("title").value(it.name)
            writer.name("type").value(
                when (it.type) {
                    Type.Good -> 0
                    Type.Bad -> 1
                }
            )
            writer.endObject()
        } ?: writer.nullValue()
    }
}