package com.example.crazy_habits

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crazy_habits.databinding.ActivityFirstBinding
import java.util.ArrayList


class FirstActivity : AppCompatActivity(), HabitAdapter.OnItemClickListener    {

    private lateinit var binding: ActivityFirstBinding
    private val habitList : MutableList<Habit> = mutableListOf()
    private lateinit var habit : Habit
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>
    private lateinit var resultLauncherEdit : ActivityResultLauncher<Intent>
    private lateinit var habitAdapter : HabitAdapter
    private lateinit var intentToSecondActivity : Intent
    private lateinit var idHabitToEdit : String

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(FirstActivity, "onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initRecyclerView()
        resultLauncherHabitEdit()

    }

   private fun  initRecyclerView () {
       binding.recyclerView.layoutManager = LinearLayoutManager(this)
       resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
           if (result.resultCode == Activity.RESULT_OK) {
               val data: Intent? = result.data
               habit = data!!.getParcelableExtra<Habit>(SecondActivity.COLLECTED_HABIT)!!
               habitList.add(habit)
               binding.recyclerView.adapter  = HabitAdapter(this)
               habitAdapter = binding.recyclerView.adapter as HabitAdapter
               habitAdapter.addMoreHabits(habitList)
               binding.recyclerView.scrollToPosition(habitAdapter.itemCount-1)
           }
       }

       binding.FAB.setOnClickListener {
           openActivityForResult()

       }

    }







    override fun onItemClicked(id: String) {
//        habitList.remove(habitList.find { it.id == id })
//        openSecondActivityEditHabit()
        idHabitToEdit = id
        intentToSecondActivity = Intent(this, SecondActivity::class.java)
        intentToSecondActivity.putExtra(HABIT_TO_EDIT, habitList.find { it.id == idHabitToEdit })
        resultLauncherEdit.launch(intentToSecondActivity)
//        habitAdapter.notifyItemRemoved(habitList.indexOf(habitList.find { it.id == id }))
//        habitAdapter.notifyItemChanged(habitList.indexOf(habitList.find { it.id == id }))
    }

    private fun resultLauncherHabitEdit () {
        resultLauncherEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                habit = data!!.getParcelableExtra<Habit>(SecondActivity.COLLECTED_HABIT)!!
                val searchHabit = habitList.find { it.id == idHabitToEdit }
                with ((searchHabit)!!) {
                   name       = habit.name
                   desc       = habit.desc
                   type       = habit.type
                   priority   = habit.priority
                   number     = habit.number
                   period     = habit.period
                   colorHabit = habit.colorHabit
                }
                habitAdapter.notifyItemChanged(habitList.indexOf(searchHabit))
            }
        }
    }



//    private fun openSecondActivityEditHabit () {
//
//        intentToSecondActivity = Intent(this, SecondActivity::class.java)
//
//        resultLauncherEdit.launch(intentToSecondActivity)
//    }




     private fun openActivityForResult() {
         intentToSecondActivity = Intent(this, SecondActivity::class.java)
         resultLauncher.launch(intentToSecondActivity)
    }


     override fun onSaveInstanceState(outState: Bundle) {
        Log.i(FirstActivity, "onSaveInstanceState")
         outState.putParcelableArrayList(RECYCLER_DATA, habitList as ArrayList<out Parcelable>)
         super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(FirstActivity, "onRestoreInstanceState")
        val mDataset = savedInstanceState.getParcelableArrayList<Habit>(RECYCLER_DATA)
        binding.recyclerView.adapter  = HabitAdapter(this)
        habitAdapter = binding.recyclerView.adapter as HabitAdapter
        habitList.addAll(mDataset as Collection<Habit>)
        habitAdapter.addMoreHabits(habitList)

    }


    /**
     * методы ЖЦ
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(FirstActivity, "onDestroy")
    }

    override fun onStop() {
        super.onStop()
        Log.d(FirstActivity, "onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.d(FirstActivity, "onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(FirstActivity, "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(FirstActivity, "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(FirstActivity, "onRestart")
    }


    companion object {
        private const val FirstActivity = "FirstActivity"
        private const val TAG           = "errorqwer"
        private const val act1_data     = "act1_data"
        private const val RECYCLER_DATA     = "recycler data"
                const val HABIT_TO_EDIT = "habitToEdit"

    }



}



