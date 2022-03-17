package com.example.crazy_habits

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val setColorButton : Button = findViewById(R.id.setColorButton)
        setColorButton.setOnClickListener{
                val intent = Intent()
                setResult(RESULT_OK, intent)
                finish()

        }
    }
}