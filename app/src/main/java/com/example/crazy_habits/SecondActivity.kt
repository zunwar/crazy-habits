package com.example.crazy_habits

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val name: EditText = findViewById(R.id.act2NameHabitText)
        val desc: EditText = findViewById(R.id.act2DescText)
        val typeRG: RadioGroup = findViewById(R.id.radioGroup)
        val priority: Spinner = findViewById(R.id.spinner)
        val number: EditText = findViewById(R.id.act2NumberText)
        val period: EditText = findViewById(R.id.act2PeriodText)
        val addButton : Button = findViewById((R.id.addButton))
        val chooseColorButton : Button = findViewById(R.id.chooseColorButton)

        addButton.setOnClickListener { view ->
            val intent = Intent()
            val type : RadioButton
            try {
                type = (findViewById(typeRG.checkedRadioButtonId))
                intent.putExtra("type", type.text.toString())
            } catch (ex:Exception){
                intent.putExtra("type", "не указано")
            }

            intent.putExtra("name", name.text.toString())
            intent.putExtra("desc", desc.text.toString())
            intent.putExtra("priority", priority.selectedItem.toString())
            intent.putExtra("number", number.text.toString())
            intent.putExtra("period", period.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }


        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
            }
        }

        fun openActivityForResult() {
            val intent = Intent(this, ThirdActivity::class.java)
            resultLauncher.launch(intent)
        }

        chooseColorButton.setOnClickListener { view ->
            openActivityForResult()
        }

            Log.d(TAG, "onCreate")
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
        private const val act2_data = "act2_data"
    }

}




