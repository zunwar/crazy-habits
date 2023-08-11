package com.example.presentation.listhabits.badlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import com.example.presentation.databinding.FragmentBadListBinding
import com.example.presentation.listhabits.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BadListFragment :
    BaseListFragment<FragmentBadListBinding?, BadListViewModel>(R.layout.fragment_bad_list) {
    private var _binding: FragmentBadListBinding? = null
    private val badListViewModel: BadListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBadListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getFragmentBinding(): FragmentBadListBinding? {
        return _binding
    }

    override fun getFragmentViewModel(): BadListViewModel {
        return badListViewModel
    }

    override fun getRecyclerView(): RecyclerView {
        return binding.recyclerViewBadHabits
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}