package com.example.crazy_habits

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController
import coil.load
import com.example.crazy_habits.databinding.ActivityFirstBinding
import com.example.presentation.listhabits.BottomSheet
import com.example.presentation.listhabits.badlist.BadListViewModel
import com.example.presentation.listhabits.goodlist.GoodListViewModel
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstActivity : AppCompatActivity() {
    private var _binding: ActivityFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var toggle: ActionBarDrawerToggle
    private val goodListViewModel: GoodListViewModel by viewModels()
    private val badListViewModel: BadListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        //пока темная тема не настроена, добавлена след строка
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        _binding = ActivityFirstBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setting ToolBar to replace the ActionBar
        setSupportActionBar(binding.appBarMain.toolbar)

        actionBarDrawerToggle()
        loadAvatar()
        filterButtonToolBar()
        sortButtonToolBar()
    }

    private fun sortButtonToolBar() {
        binding.appBarMain.filterButtonToolBar.setOnClickListener {
            bottomSheet()
        }
    }

    private fun filterButtonToolBar() {
        binding.appBarMain.sortButtonToolBar.setOnClickListener { sortButton ->
            goodListViewModel.sortClicked()
            badListViewModel.sortClicked()
            sortButton.animate().apply {
                duration = 1000
                rotationXBy(360f)
            }.start()
        }
    }

    private fun loadAvatar() {
        goodListViewModel.uri.observe(this) {
            val imgView =
                binding.navView.getHeaderView(0).findViewById<ShapeableImageView>(R.id.cat)
            imgView.load(it) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.cat_placeholder)
            }
        }
    }

    private fun actionBarDrawerToggle() {
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
                        val request = NavDeepLinkRequest.Builder
                            .fromUri("android-app://crazy_habits/viewPagerFragment".toUri())
                            .build()
                        if (!findNavController(R.id.nav_host_fragment).currentDestination!!.hasDeepLink(
                                request
                            )
                        ) findNavController(R.id.nav_host_fragment).navigate(request)
                        drawerLayout.closeDrawers()
                    }
                    R.id.nav_info -> {
                        val request = NavDeepLinkRequest.Builder
                            .fromUri("android-app://crazy_habits/infoFragment".toUri())
                            .build()
                        findNavController(R.id.nav_host_fragment).navigate(request)
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
    }

    private fun bottomSheet() {
        val bottomSheet = BottomSheet()
        bottomSheet.show(supportFragmentManager, BottomSheet.BottomSheet_TAG)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }

}