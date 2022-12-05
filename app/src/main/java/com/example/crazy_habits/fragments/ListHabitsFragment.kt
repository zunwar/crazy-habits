package com.example.crazy_habits.fragments

import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.R
import com.example.crazy_habits.adapters.HabitAdapter
import com.example.crazy_habits.databinding.FragmentListHabitsBinding
import com.example.crazy_habits.fragments.HabitEditFragment.Companion.ADD_BOOL
import com.example.crazy_habits.fragments.HabitEditFragment.Companion.HABIT_ADD
import com.example.crazy_habits.viewmodels.ListHabitsViewModel


class ListHabitsFragment : Fragment(R.layout.fragment_list_habits) {


    private var _binding: FragmentListHabitsBinding? = null
    private lateinit var habitAdapter: HabitAdapter
    private val binding get() = _binding!!
    private var createBadInstance: Boolean = false
    private val listHabitsViewModel: ListHabitsViewModel by activityViewModels{ListHabitsViewModel.Factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "List_frag_onCreate")
        if (arguments?.getBoolean(BAD_INSTANCE) != null) {
            createBadInstance = true
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
        binding.constrListHabits.doOnLayout {
            if (createBadInstance) binding.constrListHabits.setBackgroundResource(R.color.badHabit)
            else binding.constrListHabits.setBackgroundResource(R.color.goodHabit)
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(HABIT_ADD)
            ?.observe(viewLifecycleOwner) { result ->
                if (result.getBoolean(ADD_BOOL)) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val posToInsert = habitAdapter.itemCount + 1
                        binding.recyclerView.smoothScrollToPosition(posToInsert)
                    }, 10)
                }
            }
        initRecyclerView()
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
        }
        habitAdapter = binding.recyclerView.adapter as HabitAdapter

        if (createBadInstance) {
            listHabitsViewModel.badHabits.observe(viewLifecycleOwner, Observer { badHabits ->
                habitAdapter.submitList(badHabits)
            })
        } else {
            listHabitsViewModel.goodHabits.observe(viewLifecycleOwner, Observer { goodHabits ->
                habitAdapter.submitList(goodHabits)
            })
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "List_fragment_onResume")
        listHabitsViewModel.update()
    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d(TAG, "List_fragment_onPause")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d(TAG, "List_fragment_onStop")
//    }
//
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "List_fragment_onDestroy")
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
    }
//
//    override fun onDetach() {
//        super.onDetach()
//        Log.d(TAG, "List_fragment_onDetach")
//    }

    override fun onDestroyView() {
        super.onDestroyView()
//        Log.d(TAG, "List_fragment_onDestroyView")
        _binding = null
    }

    companion object {
        /**
         *
         *
         */
        const val HABIT_TO_EDIT_ID = "idHabit"
        private const val BAD_INSTANCE = "BadInstance"

        fun newInstance(bad: Boolean) =
            ListHabitsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(BAD_INSTANCE, bad)
                }
            }
    }
    val Int.dpToPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()


}