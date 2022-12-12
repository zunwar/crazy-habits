package com.example.crazy_habits.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.crazy_habits.R
import com.example.crazy_habits.adapters.ViewPagerAdapter
import com.example.crazy_habits.databinding.FragmentViewPagerBinding
import com.example.crazy_habits.fragments.HabitEditFragment.Companion.TAB_ITEM
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ViewPagerFragment :  Fragment(R.layout.fragment_view_pager) {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        val fragmentList = arrayListOf<Fragment>(
            ListHabitsFragment(),
            ListHabitsFragment.newInstance(bad = true)
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
//            requireActivity().supportFragmentManager,
            childFragmentManager,
            lifecycle
        )

        initViewPagerData(adapter)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*
переключение на фрагмент, тип привычки которого был выбран в фрагменте создания/редактирования
*/
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(TAB_ITEM)?.observe(viewLifecycleOwner){result ->
                binding.viewPagerMain.post {
                    binding.viewPagerMain.setCurrentItem(result, true)
                }
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Int>(TAB_ITEM)
        }

        fabClick()
//        viewPagerEvents()
    }


    private fun initViewPagerData(adapter : ViewPagerAdapter) {
        binding.viewPagerMain.adapter = adapter
        binding.tabLayout.tabIconTint = null
        var indicatorColorChange = true
        binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00FF37"))
        binding.tabLayout.setTabTextColors(Color.parseColor("#66000000"), Color.parseColor("#FF03DAC5"))
        binding.FAB.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00FF37"))
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
                    binding.FAB.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF0000"))
                    binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"))
                } else {
                    indicatorColorChange = true
                    binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00FF37"))
                    binding.FAB.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00FF37"))
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.alpha = 70
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Toast.makeText(activity, R.string.onTabReselectedText, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun fabClick() {
        binding.FAB.setOnClickListener {
            it.findNavController().navigate(R.id.action_viewPagerFragment_to_habitEditFragment)
        }
    }

//    private fun viewPagerEvents() {
//        binding.viewPagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            }
//
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//                super.onPageScrollStateChanged(state)
//            }
//        })
//    }



    companion object {
        const val FAB_BUTTON_CLICK = "fab button clicked"
    }

}