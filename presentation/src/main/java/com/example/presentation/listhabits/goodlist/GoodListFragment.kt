package com.example.presentation.listhabits.goodlist

import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import com.example.presentation.databinding.FragmentGoodListBinding
import com.example.presentation.listhabits.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoodListFragment :
    BaseListFragment<FragmentGoodListBinding?, GoodListViewModel>(R.layout.fragment_good_list) {

    override fun getRecyclerView(): RecyclerView {
        return binding.recyclerView
    }

}