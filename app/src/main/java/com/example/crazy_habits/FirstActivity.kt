package com.example.crazy_habits

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
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
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            add<HabitEditFragment>(R.id.fragment_container_view, tag = "habitEditTag")
                            addToBackStack("HabitEditFragment")
                        }
                   }

               }
        ))


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


        binding.apply {
            toggle = ActionBarDrawerToggle(this@FirstActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            navView.setNavigationItemSelectedListener {
                when(it.itemId){

                    R.id.nav_home -> {
                        if (savedInstanceState == null){
                            supportFragmentManager.popBackStack()
                        }
                        drawerLayout.closeDrawers()

                    }
                    R.id.nav_info -> {
                        if ((savedInstanceState == null) && !(supportFragmentManager.fragments.last().toString().contains("InfoFragment"))){
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



