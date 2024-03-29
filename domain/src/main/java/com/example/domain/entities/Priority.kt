package com.example.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Priority (val id: Int) : Parcelable {
    High (0),
    Middle ( 1),
    Low ( 2);
}