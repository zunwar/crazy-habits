package com.example.crazy_habits.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.R
import com.example.crazy_habits.adapters.HabitAdapter
import com.example.crazy_habits.databinding.FragmentListHabitsBinding
import com.example.crazy_habits.fragments.HabitEditFragment.Companion.EDIT_BOOL
import com.example.crazy_habits.fragments.HabitEditFragment.Companion.HABIT_ADD
import com.example.crazy_habits.viewmodels.ListHabitsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ListHabitsFragment : Fragment(R.layout.fragment_list_habits) {


    private var _binding: FragmentListHabitsBinding? = null
    private val binding get() = _binding!!
    private lateinit var habitAdapter: HabitAdapter
    private var isBadInstance: Boolean = false
    private val listHabitsViewModel: ListHabitsViewModel by activityViewModels{ListHabitsViewModel.Factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "List_frag_onCreate")
        arguments?.let {
            isBadInstance = it.getBoolean(BAD_INSTANCE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "List_frag_onViewCreated")

        scrollToPositionWhenAdded()
        initRecyclerView()
        setBackgroundForFragments()
    }

    private fun scrollToPositionWhenAdded() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(HABIT_ADD)
            ?.observe(viewLifecycleOwner) { result ->
                if (!result.getBoolean(EDIT_BOOL)) {
                    lifecycleScope.launch {
                        delay(100)
                        val posToInsert = habitAdapter.itemCount + 1
                        binding.recyclerView.smoothScrollToPosition(posToInsert)
                    }
                }
            }
    }

    private fun setBackgroundForFragments() {
            if (isBadInstance) binding.constrListHabits.setBackgroundResource(R.color.badHabit)
            else binding.constrListHabits.setBackgroundResource(R.color.goodHabit)
    }

    private fun initRecyclerView() {
        Log.d(TAG, "List_frag_initRecyclerView")
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = HabitAdapter{
            if (findNavController().currentDestination!!.id == R.id.viewPagerFragment) {
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToHabitEditFragment(
                        idHabit = it.id
                    )
                findNavController().navigate(action)
            }
//            listHabitsViewModel.deleteClickedHabit(it.id)
        }
        subscribeUi()
//        (binding.recyclerView.adapter as HabitAdapter).registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//            override fun onItemRangeInserted(
//                positionStart: Int,
//                itemCount: Int
//            ) {
//
//            }
//
//            override fun onChanged() {
//                super.onChanged()
//            }
//
//            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
//                super.onItemRangeChanged(positionStart, itemCount)
//            }
//
//            override fun onItemRangeRemoved(
//                positionStart: Int,
//                itemCount: Int
//            ) {
//
//            }
//        }
//        )
    }

//    fun habitClicked(){
//        if (findNavController().currentDestination!!.id == R.id.viewPagerFragment) {
//            val action =
//                ViewPagerFragmentDirections.actionViewPagerFragmentToHabitEditFragment(
//                    idHabit = it.id
//                )
//            findNavController().navigate(action)
//        }
////            listHabitsViewModel.deleteClickedHabit(it.id)
//    }

    private fun subscribeUi() {
        val listHabits = listHabitsViewModel.getRightHabits(badOrGood = isBadInstance)
        listHabits.observe(viewLifecycleOwner) { list ->
            (binding.recyclerView.adapter as HabitAdapter).submitList(list)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "List_fragment_onDestroy")
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val HABIT_TO_EDIT_ID = "idHabit"
        private const val BAD_INSTANCE = "BadInstance"

        fun newInstance(isBad: Boolean) =
            ListHabitsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(BAD_INSTANCE, isBad)
                }
            }
    }
}