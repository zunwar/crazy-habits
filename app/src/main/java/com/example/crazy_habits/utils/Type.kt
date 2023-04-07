package com.example.crazy_habits.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Type (val id : Int) : Parcelable {
    Good   (0),
    Bad    (1);
}