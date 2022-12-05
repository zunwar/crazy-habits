package com.example.crazy_habits

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Priority (val stringResId : Int, val id: Int) : Parcelable {
    High ( R.string.highPriority, 0),
    Middle (R.string.middlePriority, 1),
    Low ( R.string.lowPriority, 2);



    override fun toString(): String {
        return stringResId.toString()
    }
}