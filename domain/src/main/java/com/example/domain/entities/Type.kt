package com.example.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Type (val id : Int) : Parcelable {
    Good   (0),
    Bad    (1);
}