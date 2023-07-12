package com.example.presentation.listhabits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.entities.MessageDoHabit
import com.example.presentation.R
import com.example.theme.R.color
import com.example.presentation.databinding.FragmentListHabitsBinding
import com.example.presentation.edithabit.HabitEditFragment.Companion.EDIT_BOOL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListHabitsFragment : Fragment(R.layout.fragment_list_habits) {

    private var _binding: FragmentListHabitsBinding? = null
    private val binding get() = _binding!!
    private var isBadInstance: Boolean = false
    private val listHabitsViewModel: ListHabitsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isBadInstance = it.getBoolean(BAD_INSTANCE)
        }
        syncHabitsWithServer(isBadInstance)
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
        setBackgroundForFragments()
        initRecyclerView()
        scrollToPositionWhenNewAdded()
    }

    private fun syncHabitsWithServer(isBadInstance: Boolean) {
        listHabitsViewModel.syncHabitsWithServer(isBadInstance)
    }

    private fun scrollToPositionWhenNewAdded() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(EDIT_BOOL)
            ?.observe(viewLifecycleOwner) { result ->
                listHabitsViewModel.listLoadedToRecycler.observe(viewLifecycleOwner) {
                    if (!result) {
                        val posToInsert = (binding.recyclerView.adapter as HabitAdapter).itemCount
                        binding.recyclerView.smoothScrollToPosition(posToInsert)
                        findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(
                            EDIT_BOOL
                        )
                    }
                }
            }
    }

    private fun setBackgroundForFragments() {
        if (isBadInstance) binding.constrListHabits.setBackgroundResource(color.badHabit)
        else binding.constrListHabits.setBackgroundResource(color.goodHabit)
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = HabitAdapter(
            onItemClicked = {
                val action = ViewPagerFragmentDirections.actionViewPagerFragmentToHabitEditFragment(
                    idHabit = it.id
                )
                findNavController().navigate(action)
            },
            onItemLongClicked = {
                listHabitsViewModel.deleteClickedHabit(it.id)
                Toast.makeText(context, getString(R.string.habit_delete), Toast.LENGTH_SHORT).show()
            },
            onDoHabitClicked = {
                listHabitsViewModel.doHabitClicked(habit = it)
                    .observe(viewLifecycleOwner) { msgData ->
                        val message =
                            when (msgData.first) {
                                MessageDoHabit.DoBadHabitMore -> getString(
                                    R.string.do_bad_habit_more,
                                    "${msgData.second}"
                                )
                                MessageDoHabit.DoBadHabitEnough -> getString(R.string.do_bad_habit_enough)
                                MessageDoHabit.DoGoodHabitMore -> getString(
                                    R.string.do_good_habit_more,
                                    "${msgData.second}"
                                )
                                MessageDoHabit.DoGoodHabitEnough -> getString(R.string.do_good_habit_enough)
                            }
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
                    }
            }
        )
        subscribeUi()
    }

    private fun subscribeUi() {
        listHabitsViewModel.getGoodOrBadList(isBadList = isBadInstance)
            .observe(viewLifecycleOwner) { list ->
                (binding.recyclerView.adapter as HabitAdapter).submitList(list)
                listHabitsViewModel.listLoadedToRecycler(true)
            }
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