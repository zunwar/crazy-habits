package com.example.crazy_habits

import android.os.Parcel
import android.os.Parcelable

data class Habit(
    val name:        String,
    val desc:        String,
    val type:        String,
    val priority:    String,
    val number:      String,
    val period:      String,
    val colorHabit:  Int,
    val id : String
) : Parcelable {



    init {
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(desc)
        parcel.writeString(type)
        parcel.writeString(priority)
        parcel.writeString(number)
        parcel.writeString(period)
        parcel.writeInt(colorHabit)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Habit> {
        override fun createFromParcel(parcel: Parcel): Habit {
            return Habit(parcel)
        }

        override fun newArray(size: Int): Array<Habit?> {
            return arrayOfNulls(size)
        }
    }
}
