package com.example.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ColorBox(
    val id: String,
    val color: Int,
    val colorBoxNum: ColorBoxNum
) : Parcelable