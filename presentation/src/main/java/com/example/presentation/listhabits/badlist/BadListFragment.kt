package com.example.presentation.listhabits.badlist

import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import com.example.presentation.databinding.FragmentBadListBinding
import com.example.presentation.listhabits.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BadListFragment :
    BaseListFragment<FragmentBadListBinding?, BadListViewModel>(R.layout.fragment_bad_list) {

    override fun getRecyclerView(): RecyclerView {
        return binding.recyclerViewBadHabits
    }

}