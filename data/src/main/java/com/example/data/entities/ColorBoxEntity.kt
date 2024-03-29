package com.example.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.entities.ColorBoxNum

@Entity
data class ColorBoxEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "color") val color: Int,
    @ColumnInfo(name = "colorBoxNum") val colorBoxNum: ColorBoxNum
)