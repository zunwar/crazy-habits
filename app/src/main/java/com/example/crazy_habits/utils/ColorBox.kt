package com.example.crazy_habits.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ColorBox(
    val id: String,
    val color: Int,
    val colorBoxNum: ColorBoxNum
) : Parcelable