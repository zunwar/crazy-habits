package com.example.crazy_habits

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Priority (val id: Int) : Parcelable {
    High (0),
    Middle ( 1),
    Low ( 2);
}