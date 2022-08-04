package com.example.crazy_habits

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.*
import com.example.crazy_habits.databinding.ActivityFirstBinding
import com.example.crazy_habits.databinding.FragmentListHabitsBinding
import com.example.crazy_habits.fragments.*


class FirstActivity : AppCompatActivity()    {
//class FirstActivity : AppCompatActivity(), HabitAdapter.OnItemClickListener    {

    private lateinit var binding: ActivityFirstBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private val habitList : MutableList<Habit> = mutableListOf()
    private lateinit var habit : Habit
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>
    private lateinit var resultLauncherEdit : ActivityResultLauncher<Intent>
    private lateinit var habitAdapter : HabitAdapter
    private lateinit var intentToSecondActivity : Intent
    private lateinit var idHabitToEdit : String
    private lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(FirstActivity, "onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setting ToolBar to replace the ActionBar
        setSupportActionBar(binding.appBarMain.toolbar)


        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<ListHabitsFragment>(R.id.fragment_container_view)
            }
        }
        supportFragmentManager.setFragmentResultListener("frag1_addNewHabitButton", this, FragmentResultListener(
               fun(requestkey : String, bundle: Bundle) {
                   if (savedInstanceState == null) {
                       supportFragmentManager.commit {
                           setReorderingAllowed(true)
                           add<HabitEditFragment>(R.id.fragment_container_view, tag = "habitEditTag")
                           addToBackStack("HabitEditFragment")
                       }
                   } else {
////                       val fragmnet2 : HabitEditFragment? = supportFragmentManager.findFragmentByTag("habitEditTag") as HabitEditFragment?
////                       supportFragmentManager.commit {
////                           setReorderingAllowed(true)
////                           if (fragmnet2 != null) {
////                               show(fragmnet2)
////                           }
////                       }
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            add<HabitEditFragment>(R.id.fragment_container_view, tag = "habitEditTag")
                            addToBackStack("HabitEditFragment")
                        }
                   }

               }
        ))

//        supportFragmentManager.setFragmentResultListener("frag2_chooseColorButton", this, FragmentResultListener(
//            fun(requestkey : String, bundle: Bundle) {
//                supportFragmentManager.commit {
//                    setReorderingAllowed(true)
//                    replace<ColorHabitFragment>(R.id.fragment_container_view)
//                    addToBackStack("ColorHabitFragment")
//                }
//
//            }
//        ))


        supportFragmentManager.setFragmentResultListener(ListHabitsFragment.HABIT_TO_EDIT, this, FragmentResultListener(
            fun(requestkey : String, bundle: Bundle) {
                if (savedInstanceState == null) {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        add<HabitEditFragment>(R.id.fragment_container_view, args = bundle)
                        addToBackStack("HabitEditFragment")
                    }
                }

            }
        ))


        supportFragmentManager.setFragmentResultListener("frag2_chooseColorButton", this, FragmentResultListener(
            fun(requestkey : String, bundle: Bundle) {
                if (savedInstanceState == null) {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        add<ColorHabitFragment>(R.id.fragment_container_view, args = bundle)
                        addToBackStack("ColorHabitFragment")
                    }
                }
            }
        ))

//        supportFragmentManager.setFragmentResultListener("added", this, FragmentResultListener(
//            fun (requstKey : String , bundle : Bundle) {
//                Log.d(TAG, "нажали кнопку")
//                supportFragmentManager.popBackStack()
//                Log.d(TAG, "popbackstack")
//            }
//        ))

//        supportFragmentManager.setFragmentResultListener("frag2_AddButton", this, FragmentResultListener(
//            fun(requestkey : String, qwebundle: Bundle) {
//                habit = qwebundle.getParcelable<Habit>(COLLECTED_HABIT)!!
//                supportFragmentManager.commit {
//                    setReorderingAllowed(true)
//                    replace<ListHabitsFragment>(R.id.fragment_container_view)
//                    addToBackStack("ListHabitsFragment")
//                }
//            }
//        ))

        binding.apply {
            toggle = ActionBarDrawerToggle(this@FirstActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            navView.setNavigationItemSelectedListener {
                when(it.itemId){

                    R.id.nav_home -> {
                        Toast.makeText(this@FirstActivity, "first item clicked", Toast.LENGTH_SHORT).show()
                    }
                    R.id.nav_info -> {
                        if (savedInstanceState == null) {
                            supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                add<InfoFragment>(R.id.fragment_container_view)
                                addToBackStack("InfoFragment")
                            }
                        }
                        drawerLayout.closeDrawers()
                    }
                    R.id.nav_emotion -> {
                        Toast.makeText(this@FirstActivity, "Feels good", Toast.LENGTH_SHORT).show()
                    }
                }
                true
                }
            }



    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return  true
        }
        return super.onOptionsItemSelected(item)
    }

//   private fun  initRecyclerView () {
//       binding.recyclerView.layoutManager = LinearLayoutManager(this)
//       resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//           if (result.resultCode == Activity.RESULT_OK) {
//               val data: Intent? = result.data
//               habit = data!!.getParcelableExtra<Habit>(SecondActivity.COLLECTED_HABIT)!!
//               habitList.add(habit)
//               binding.recyclerView.adapter  = HabitAdapter(this)
//               habitAdapter = binding.recyclerView.adapter as HabitAdapter
//               habitAdapter.addMoreHabits(habitList)
//               binding.recyclerView.scrollToPosition(habitAdapter.itemCount-1)
//           }
//       }
//
//       binding.FAB.setOnClickListener {
////           openActivityForResult()
////           supportFragmentManager
////               .beginTransaction()
////               .add(R.id.fragmentListHabits, ListHabitsFragment)
////               .commit()
//       }
//
//    }
//
//
//
//
//
//
//
//    override fun onItemClicked(id: String) {
////        habitList.remove(habitList.find { it.id == id })
////        openSecondActivityEditHabit()
//        idHabitToEdit = id
//        intentToSecondActivity = Intent(this, SecondActivity::class.java)
//        intentToSecondActivity.putExtra(HABIT_TO_EDIT, habitList.find { it.id == idHabitToEdit })
//        resultLauncherEdit.launch(intentToSecondActivity)
////        habitAdapter.notifyItemRemoved(habitList.indexOf(habitList.find { it.id == id }))
////        habitAdapter.notifyItemChanged(habitList.indexOf(habitList.find { it.id == id }))
//    }
//
//    private fun resultLauncherHabitEdit () {
//        resultLauncherEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data: Intent? = result.data
//                habit = data!!.getParcelableExtra<Habit>(SecondActivity.COLLECTED_HABIT)!!
//                val searchHabit = habitList.find { it.id == idHabitToEdit }
//                with ((searchHabit)!!) {
//                   name       = habit.name
//                   desc       = habit.desc
//                   type       = habit.type
//                   priority   = habit.priority
//                   number     = habit.number
//                   period     = habit.period
//                   colorHabit = habit.colorHabit
//                }
//                habitAdapter.notifyItemChanged(habitList.indexOf(searchHabit))
//            }
//        }
//    }
//
//
//
////    private fun openSecondActivityEditHabit () {
////
////        intentToSecondActivity = Intent(this, SecondActivity::class.java)
////
////        resultLauncherEdit.launch(intentToSecondActivity)
////    }
//
//
//
//
//     private fun openActivityForResult() {
//         intentToSecondActivity = Intent(this, SecondActivity::class.java)
//         resultLauncher.launch(intentToSecondActivity)
//    }
//
//
//     override fun onSaveInstanceState(outState: Bundle) {
//        Log.i(FirstActivity, "onSaveInstanceState")
//         outState.putParcelableArrayList(RECYCLER_DATA, habitList as ArrayList<out Parcelable>)
//         super.onSaveInstanceState(outState)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        Log.i(FirstActivity, "onRestoreInstanceState")
//        val mDataset = savedInstanceState.getParcelableArrayList<Habit>(RECYCLER_DATA)
//        binding.recyclerView.adapter  = HabitAdapter(this)
//        habitAdapter = binding.recyclerView.adapter as HabitAdapter
//        habitList.addAll(mDataset as Collection<Habit>)
//        habitAdapter.addMoreHabits(habitList)
//
//    }
//
//
//    /**
//     * методы ЖЦ
//     */
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d(FirstActivity, "onDestroy")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d(FirstActivity, "onStop")
//    }
//
//    override fun onStart() {
//        super.onStart()
//        Log.d(FirstActivity, "onStart")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d(FirstActivity, "onPause")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.d(FirstActivity, "onResume")
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        Log.d(FirstActivity, "onRestart")
//    }


    companion object {
        private const val FirstActivity = "FirstActivity"
        const val TAG                   = "errorqwer"
        private const val act1_data     = "act1_data"
        private const val RECYCLER_DATA     = "recycler data"
                const val HABIT_TO_EDIT = "habitToEdit"

    }



}



