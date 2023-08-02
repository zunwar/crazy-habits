package com.example.presentation.listhabits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.domain.entities.MessageDoHabit
import com.example.presentation.R
import com.example.presentation.edithabit.HabitEditFragment
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseListFragment<
        Binding : ViewBinding?,
        ViewModel : BaseListViewModel
        >(
    @LayoutRes layoutID: Int
) : Fragment(layoutID) {
    private lateinit var viewModel: ViewModel
    private var _binding: Binding? = null
    protected val binding get() = _binding!!
    private val type = (javaClass.genericSuperclass as ParameterizedType)
    private val classViewBinding = type.actualTypeArguments[0] as Class<Binding>
    private val classViewModel = type.actualTypeArguments[1] as Class<ViewModel>
    private val inflateMethod = classViewBinding.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
    private var recycler: RecyclerView? = null
    protected abstract fun getRecyclerView(): RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateMethod.invoke(null, inflater, container, false) as Binding
        viewModel = createViewModelLazy(classViewModel.kotlin, { requireActivity().viewModelStore }).value
        syncHabitsWithServer()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = getRecyclerView()
        initRecyclerView()
        scrollToPositionWhenNewAdded()
    }


    private fun syncHabitsWithServer() {
        viewModel.syncHabitsWithServer()
    }

    private fun scrollToPositionWhenNewAdded() {
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
                Toast.makeText(context, getString(R.string.habit_delete), Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}