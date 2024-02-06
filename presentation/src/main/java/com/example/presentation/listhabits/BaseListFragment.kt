package com.example.presentation.listhabits

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.domain.entities.DeleteStatus
import com.example.domain.entities.MessageDoHabit
import com.example.presentation.R
import com.example.presentation.edithabit.HabitEditFragment

@Suppress("UNCHECKED_CAST")
abstract class BaseListFragment<
        Binding : ViewBinding?,
        ViewModel : BaseListViewModel
        >(
    @LayoutRes layoutID: Int
) : Fragment(layoutID) {

    private val _binding: Binding? by lazy { this.getFragmentBinding() }
    protected val binding get() = _binding!!
    private val viewModel: ViewModel by lazy { getFragmentViewModel() }
    private val recycler: RecyclerView? by lazy { getRecyclerView() }

    protected abstract fun getFragmentBinding(): Binding
    protected abstract fun getFragmentViewModel(): ViewModel
    protected abstract fun getRecyclerView(): RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        syncHabitsWithServer()
        initRecyclerView()
        scrollToPositionWhenNewAdded()
    }

    private fun syncHabitsWithServer() {
            viewModel.syncHabitsWithServer()
    }

    private fun scrollToPositionWhenNewAdded() {
            try {
                findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
                    HabitEditFragment.EDIT_BOOL
                )
                    ?.observe(viewLifecycleOwner) { result ->
                        viewModel.listLoadedToRecycler.observe(viewLifecycleOwner) {
                            if (!result) {
                                val posToInsert = (recycler!!.adapter as HabitAdapter).itemCount
                                recycler!!.smoothScrollToPosition(posToInsert)
                                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(
                                    HabitEditFragment.EDIT_BOOL
                                )
                            }
                        }
                    }
            } finally {}
    }

    private fun initRecyclerView() {
        recycler?.adapter = HabitAdapter(
            onItemClicked = {
                val action = ViewPagerFragmentDirections.actionViewPagerFragmentToHabitEditFragment(
                    idHabit = it.id
                )
                findNavController().navigate(action)
            },
            onItemLongClicked = {
                    viewModel.deleteClickedHabit(it.id)
                        .observe(viewLifecycleOwner) { deleteStatus ->
                            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA") val message =
                                when (deleteStatus) {
                                    DeleteStatus.Deleted -> getString(R.string.habit_delete)
                                    DeleteStatus.ErrorOccurred -> getString(R.string.error_occurred)
                                }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
            },
            onDoHabitClicked = {
                viewModel.doHabitClicked(habit = it)
                    .observe(viewLifecycleOwner) { msgData ->
                        val message =
                            when (msgData.first) {
                                MessageDoHabit.DoBadHabitMore -> resources.getQuantityString(
                                    R.plurals.do_bad_habit_more,
                                    msgData.second, msgData.second
                                )
                                MessageDoHabit.DoBadHabitEnough -> getString(R.string.do_bad_habit_enough)
                                MessageDoHabit.DoGoodHabitMore -> resources.getQuantityString(
                                    R.plurals.do_good_habit_more,
                                    msgData.second, msgData.second
                                )
                                MessageDoHabit.DoGoodHabitEnough -> getString(R.string.do_good_habit_enough)
                            }
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
                    }
            }
        )
        subscribeRecycler()
    }

    private fun subscribeRecycler() {
            viewModel.getHabitsList()
                .observe(viewLifecycleOwner) { list ->
                    (recycler?.adapter as HabitAdapter).submitList(list)
                    viewModel.listLoadedToRecycler(true)
                }
    }

}