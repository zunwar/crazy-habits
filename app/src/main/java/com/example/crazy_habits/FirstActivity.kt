package com.example.crazy_habits

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.graphics.Color
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
import androidx.viewpager.widget.PagerAdapter
import com.example.crazy_habits.adapters.ViewPagerAdapter
import com.example.crazy_habits.databinding.ActivityFirstBinding
import com.example.crazy_habits.databinding.FragmentListHabitsBinding
import com.example.crazy_habits.fragments.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


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

        initViewPagerData()



//теперь viewpager дергает фрагменты
//        if (savedInstanceState == null) {
//            supportFragmentManager.commit {
//                setReorderingAllowed(true)
//                replace<ListHabitsFragment>(R.id.fragment_container_view)
//            }
//        }


        supportFragmentManager.setFragmentResultListener("frag1_addNewHabitButton", this, FragmentResultListener(
               fun(requestkey : String, bundle: Bundle) {
                   if (savedInstanceState == null) {
                       supportFragmentManager.commit {
                           setReorderingAllowed(true)
                           add<HabitEditFragment>(R.id.fragment_container_view2, tag = "habitEditTag")
                           addToBackStack("HabitEditFragment")
                       }
                   } else {
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            add<HabitEditFragment>(R.id.fragment_container_view2, tag = "habitEditTag")
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
                        add<HabitEditFragment>(R.id.fragment_container_view2, args = bundle)
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
                        add<ColorHabitFragment>(R.id.fragment_container_view2, args = bundle)
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
                                add<InfoFragment>(R.id.fragment_container_view2)
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

    private fun initViewPagerData() {
        binding.viewPagerMain.adapter = ViewPagerAdapter(this)
        binding.tabLayout.tabIconTint = null
        var indicatorColorChange = true
        binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00FF37"))
        TabLayoutMediator(binding.tabLayout, binding.viewPagerMain){
            tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.emoji_angel_c)
                    tab.setText(R.string.goodHabitTab1)
                }
                1 -> {
                    tab.setIcon(R.drawable.emoji_evil_c)
                    tab.setText(R.string.badHabitTab2)
                    tab.icon?.alpha = 70

                }
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon?.alpha = 255
                if (indicatorColorChange) {
                    indicatorColorChange = false
                    binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"))
                } else {
                    indicatorColorChange = true
                    binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00FF37"))
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.alpha = 70
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Toast.makeText(this@FirstActivity, "Already here", Toast.LENGTH_SHORT).show()
            }

        })



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



