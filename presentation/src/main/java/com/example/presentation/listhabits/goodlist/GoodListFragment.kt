package com.example.presentation.listhabits.goodlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import com.example.presentation.databinding.FragmentGoodListBinding
import com.example.presentation.listhabits.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoodListFragment :
    BaseListFragment<FragmentGoodListBinding?, GoodListViewModel>(R.layout.fragment_good_list) {
    private var _binding: FragmentGoodListBinding? = null
    private val goodListViewModel: GoodListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoodListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getFragmentBinding(): FragmentGoodListBinding? {
        return _binding
    }

    override fun getFragmentViewModel(): GoodListViewModel {
        return goodListViewModel
    }

    override fun getRecyclerView(): RecyclerView {
        return binding.recyclerView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}