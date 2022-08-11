package com.example.crazy_habits.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.crazy_habits.fragments.BadHabitsFragment
import com.example.crazy_habits.fragments.InfoFragment
import com.example.crazy_habits.fragments.ListHabitsFragment

//class ViewPagerAdapter(activity: AppCompatActivity, val itemsCount: Int): FragmentStateAdapter(activity) {
class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
//        return itemsCount
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ListHabitsFragment()
            else -> BadHabitsFragment()
        }
    }




}