package com.example.presentation.edithabit

import android.graphics.Color
import android.graphics.drawable.GradientDrawable

class ShapeColorBox(
    strokeWidth : Int,
    colorOfBox : Int
) : GradientDrawable() {
    init {
        cornerRadius = 15f
        setStroke(strokeWidth, Color.parseColor("#EAEAEA"))
        setColor(colorOfBox)
    }
}