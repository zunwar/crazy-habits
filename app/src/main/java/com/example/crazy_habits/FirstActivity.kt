package com.example.crazy_habits

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.example.crazy_habits.databinding.ActivityFirstBinding
import com.example.crazy_habits.listhabits.BottomSheet
import com.example.crazy_habits.listhabits.ListHabitsViewModel


class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private val listHabitsViewModel: ListHabitsViewModel by viewModels{ ListHabitsViewModel.Factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        Log.d(TAG, "FirstActivity---onCreate ")
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setting ToolBar to replace the ActionBar
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@FirstActivity,
                drawerLayout,
                R.string.open,
                R.string.close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_home -> {
                        findNavController(R.id.nav_host_fragment).popBackStack(
                            R.id.viewPagerFragment,
                            false
                        )
                        drawerLayout.closeDrawers()
                    }
                    R.id.nav_info -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_infoFragment)
                        drawerLayout.closeDrawers()
                    }
                    R.id.nav_emotion -> {
                        Toast.makeText(
                            this@FirstActivity,
                            R.string.nav_emotionText,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                true
            }
        }

        binding.appBarMain.filterButtonToolBar.setOnClickListener {
            bottomSheet()
        }

        binding.appBarMain.sortButtonToolBar.setOnClickListener { sortButton ->
            listHabitsViewModel.sortClicked()
            sortButton.animate().apply {
                duration = 1000
                rotationXBy(360f)
            }.start()
        }
    }

    private fun bottomSheet() {
        val bottomSheet = BottomSheet()
        bottomSheet.show(supportFragmentManager, BottomSheet.BottomSheet_TAG)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }

    companion object {
        const val TAG = "errorqwer"
    }

}



