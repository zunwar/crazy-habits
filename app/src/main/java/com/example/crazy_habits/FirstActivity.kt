package com.example.crazy_habits

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.example.crazy_habits.databinding.ActivityFirstBinding
import com.example.crazy_habits.fragments.BottomSheet


class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        Log.d(FirstActivity, "onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setting ToolBar to replace the ActionBar
        setSupportActionBar(binding.appBarMain.toolbar)


        binding.apply {
            toggle = ActionBarDrawerToggle(this@FirstActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            navView.setNavigationItemSelectedListener {
                when(it.itemId){

                    R.id.nav_home -> {
                        findNavController(R.id.nav_host_fragment).popBackStack(R.id.viewPagerFragment , false)
                        drawerLayout.closeDrawers()

                    }
                    R.id.nav_info -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_infoFragment)
                        drawerLayout.closeDrawers()
                    }
                    R.id.nav_emotion -> {
                        Toast.makeText(this@FirstActivity, R.string.nav_emotionText, Toast.LENGTH_SHORT).show()
                    }
                }
                true
                }
            }

        binding.appBarMain.filterButtonToolBar.setOnClickListener {
            bottomSheet()
        }



    }

    private fun bottomSheet() {
        val bottomSheet = BottomSheet()
        bottomSheet.show(supportFragmentManager, BottomSheet.BottomSheet_TAG)
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
    }



}



