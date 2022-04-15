package com.example.crazy_habits

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import com.example.crazy_habits.databinding.ActivitySecondBinding
import java.util.*


class SecondActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySecondBinding
    lateinit var habit : Habit
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>
    private lateinit var oldHabit: Habit


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(SecondActivity, "onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        binding.radioGroup.check(binding.radioButton2.id)
        var type = ""
        var colorHabit = 0
        var edit = false

        if (intent.getParcelableExtra<Habit>(FirstActivity.HABIT_TO_EDIT) !== null) {
            oldHabit = intent.getParcelableExtra(FirstActivity.HABIT_TO_EDIT)!!
            with (oldHabit) {
                if (name == getString(R.string.notSet)) binding.NameHabitText.setText("") else  binding.NameHabitText.setText(name)
                binding.DescText.setText(desc)
                when (oldHabit.type) {
                    getString(R.string.goodHabit)   -> binding.radioGroup.check(binding.radioButton1.id)
                    getString(R.string.neutralHabit) -> binding.radioGroup.check(binding.radioButton2.id)
                    getString(R.string.badHabit)     -> binding.radioGroup.check(binding.radioButton3.id)
                }
                when (oldHabit.priority) {
                    getString(R.string.empty) -> binding.prioritySpinner.setSelection(0)
                    getString(R.string.highPriority) -> binding.prioritySpinner.setSelection(1)
                    getString(R.string.middlePriority) -> binding.prioritySpinner.setSelection(2)
                    getString(R.string.lowPriority) -> binding.prioritySpinner.setSelection(3)
                }
                if (number == getString(R.string.empty)) binding.NumberText.setText("") else binding.NumberText.setText(number)
                if (period == getString(R.string.empty)) binding.PeriodText.setText("") else binding.PeriodText.setText(period)
                binding.colorOfHabit.background = ShapeColorBox(1, oldHabit.colorHabit)
            }
            binding.addButton.text = getString(R.string.changeButton)
            if (binding.radioGroup.checkedRadioButtonId != -1 ) edit = true

        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                colorHabit = data!!.getIntExtra(COLOR_HABIT, 0)
                binding.colorOfHabit.background = ShapeColorBox(1, colorHabit)
            }
        }

        binding.radioGroup.forEach {
            it.setOnClickListener{
               type = findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString()
            }
        }




        binding.addButton.setOnClickListener {
//            type = findViewById( binding.radioGroup.checkedRadioButtonId)
            habit = Habit(
                name       = binding.NameHabitText.text.toString(),
                desc       = binding.DescText.text.toString(),
                type       = if (edit) (findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString()) else type,
                priority   = binding.prioritySpinner.selectedItem.toString(),
                number     = binding.NumberText.text.toString(),
                period     = binding.PeriodText.text.toString(),
                colorHabit = colorHabit,
                        id = UUID.randomUUID().toString()
            )
            intent.apply {
                            val bundle = Bundle().apply {
                                putExtra(COLLECTED_HABIT , habit)
                            }
                            putExtras(bundle)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.chooseColorButton.setOnClickListener {
            openActivityForResult()
        }

    }

    private fun openActivityForResult() {
        val intent = Intent(this, ThirdActivity::class.java)
        resultLauncher.launch(intent)

    }


    /**
     * методы ЖЦ
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(SecondActivity, "onDestroy")
    }

    override fun onStop() {
        super.onStop()
        Log.d(SecondActivity, "onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.d(SecondActivity, "onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(SecondActivity, "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(SecondActivity, "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(SecondActivity, "onRestart")
    }

    companion object {
        private const val SecondActivity = "SecondActivity"
        private const val TAG = "errorqwer"
        private const val COLOR_HABIT = "colorHabit"
        const val COLLECTED_HABIT = "collectedHabit"
        private const val act2_data = "act2_data"
    }

}




