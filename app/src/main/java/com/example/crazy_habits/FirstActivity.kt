package com.example.crazy_habits

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class FirstActivity : AppCompatActivity() , View.OnClickListener   {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        val button1: Button = findViewById(R.id.button1)
        button1.setOnClickListener(this)
        Log.d(TAG, "onCreate")
     }

     override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        val counter1: TextView = findViewById(R.id.counter1)
        counter1.text = (counter1.text.toString().toInt()+1).toString()
        outState.putString(act1_data, counter1.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState")
        val counter1:TextView = findViewById(R.id.counter1)
        counter1.text = savedInstanceState.getString(act1_data)

    }

    override fun onClick(view: View?) {
        val counter1 = findViewById<TextView>(R.id.counter1)
        val counter1Data: Int
        val sendIntent = Intent(this, SecondActivity::class.java)
            .apply {
            val bundle = Bundle().apply {
            counter1Data = counter1.text.toString().toInt()
                putInt(act1_data , counter1Data)
            }
            putExtras(bundle)
        }
        startActivity(sendIntent)
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
        private const val TAG = "FirstActivity"
        private const val act1_data = "act1_data"
    }

}


