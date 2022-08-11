package com.example.crazy_habits

import android.os.Parcel
import android.os.Parcelable

enum class Type (val type: String,val id : Int) : Parcelable {

    NoSet  ("не указано", 0),
    Empty  ("---", 1),
    Good   ("Полезная", 2),
    Bad    ("Плохая", 3);

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Type> {
        override fun createFromParcel(parcel: Parcel): Type {
            return values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<Type?> {
            return arrayOfNulls(size)
        }
    }
}