package com.example.crazy_habits

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class SecondActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val counter2: TextView = findViewById(R.id.counter2)
        val counter1Data = 0
        val subData = intent.getIntExtra("act1_data", counter1Data)
        counter2.text = (subData*subData).toString()
        val button2: Button = findViewById(R.id.button2)
        button2.setOnClickListener(this)
    Log.d(TAG, "onCreate")
    }

    override fun onClick(view: View?) {
       onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }
    companion object {
        private const val TAG = "SecondActivity"
    }

}




