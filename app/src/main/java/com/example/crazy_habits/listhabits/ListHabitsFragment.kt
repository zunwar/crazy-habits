package com.example.crazy_habits.listhabits

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.R
import com.example.crazy_habits.databinding.FragmentListHabitsBinding
import com.example.crazy_habits.edithabits.HabitEditFragment.Companion.EDIT_BOOL


class ListHabitsFragment : Fragment(R.layout.fragment_list_habits) {

    private var _binding: FragmentListHabitsBinding? = null
    private val binding get() = _binding!!
    private var isBadInstance: Boolean = false
    private val listHabitsViewModel: ListHabitsViewModel by activityViewModels{ ListHabitsViewModel.Factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "List_frag_onCreate")
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
        Log.d(TAG, "List_frag_onViewCreated")
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
            if (isBadInstance) binding.constrListHabits.setBackgroundResource(R.color.badHabit)
            else binding.constrListHabits.setBackgroundResource(R.color.goodHabit)
    }

    private fun initRecyclerView() {
        Log.d(TAG, "List_frag_initRecyclerView")
        binding.recyclerView.adapter = HabitAdapter{
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToHabitEditFragment(
                        idHabit = it.id
                    )
                findNavController().navigate(action)
//            закоментировать часть выше и раскоментировать эту строку для удаления по клику
//            listHabitsViewModel.deleteClickedHabit(it.id)
        }
        subscribeUi()
    }

    private fun subscribeUi() {
        listHabitsViewModel.getGoodOrBadList(isBadList = isBadInstance)
            .observe(viewLifecycleOwner) { list ->
                (binding.recyclerView.adapter as HabitAdapter).submitList(list)
                listHabitsViewModel.listLoadedToRecycler(true)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "List_fragment_onDestroy")
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