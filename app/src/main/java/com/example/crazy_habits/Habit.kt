package com.example.crazy_habits

import android.os.Parcel
import android.os.Parcelable

data class Habit(
    var name:        String,
    var desc:        String,
    var type:        String,
    var priority:    String,
    var number:      String,
    var period:      String,
    var colorHabit:  Int = 16777215,
    var id : String
) : Parcelable {



    private fun checkData(parameter : String) : String {
        return parameter.ifEmpty {Type.Empty.type}
    }

    init {
        name   = name.ifEmpty { Type.NoSet.type }
//        desc   = checkData(desc)
        type   = checkData(type)
        number = checkData(number)
        period = checkData(period)
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
