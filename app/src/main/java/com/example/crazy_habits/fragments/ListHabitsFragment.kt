package com.example.crazy_habits.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.Habit
import com.example.crazy_habits.adapters.HabitAdapter
import com.example.crazy_habits.R
import com.example.crazy_habits.Type
import com.example.crazy_habits.databinding.FragmentListHabitsBinding
import com.example.crazy_habits.fragments.HabitEditFragment.Companion.COLLECTED_HABIT
import com.example.crazy_habits.fragments.HabitEditFragment.Companion.HABIT_ADD
import com.example.crazy_habits.viewmodels.ListHabitsViewModel
import java.util.*

class ListHabitsFragment : Fragment(R.layout.fragment_list_habits), HabitAdapter.OnItemClickListener {


    private var _binding: FragmentListHabitsBinding? = null
    private lateinit var habit : Habit
    private lateinit var habitAdapter : HabitAdapter
    private val binding get() = _binding!!
    private var createBadInstance : Boolean = false
    private val listHabitsViewModel : ListHabitsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "List_frag_onCreate")
        if (arguments?.getBoolean(BAD_INSTANCE) != null ) {
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
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(HABIT_ADD)?.observe(viewLifecycleOwner) {result ->
            habit = result.getParcelable<Habit>(COLLECTED_HABIT)!!
            if (listHabitsViewModel.isChange(habit)) {
                val posToInsert = habitAdapter.itemCount+1
                binding.recyclerView.smoothScrollToPosition(posToInsert)
            }
        }
        initRecyclerView()
    }

    override fun onItemClicked(id: String) {
        val bundle = Bundle()
//        bundle.putParcelable(HABIT_TO_EDIT, habitList.find { it.id == id })
        bundle.putParcelable(HABIT_TO_EDIT, listHabitsViewModel.getHabitToEdit(id))
        findNavController().navigate(R.id.action_viewPagerFragment_to_habitEditFragment, bundle)

//        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
//            MOVE
//        )?.observe(viewLifecycleOwner) { result ->
//        val index = habitList.indexOf(habitList.find { it.id == id })
//        habitList.removeAt(index)
//        habitAdapter.notifyItemRemoved(index)
//        }

//            habitAdapter.notifyItemRemoved(habitList.indexOf(habitList.find { it.id == id }))
//        habitAdapter.notifyItemChanged(habitList.indexOf(habitList.find { it.id == id }))
    }


    private fun  initRecyclerView () {
        Log.d(TAG, "List_frag_initRecyclerView")
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter  = HabitAdapter(this)
        habitAdapter = binding.recyclerView.adapter as HabitAdapter
//        habitAdapter.addMoreHabits(habitList)


        if (createBadInstance) {
            listHabitsViewModel.habit.observe(viewLifecycleOwner, Observer { list ->
//                habitAdapter.addMoreHabits(list.filter { it.type == Type.Bad.type })
                habitAdapter.addMoreHabits(listHabitsViewModel.getBadHabits())
            })
        }
        else {
            listHabitsViewModel.habit.observe(viewLifecycleOwner, Observer { list ->
//                habitAdapter.addMoreHabits(list.filter { it.type == Type.Good.type })
                habitAdapter.addMoreHabits(listHabitsViewModel.getGoodHabits())
            })
        }
    }


//    private fun changeHabit () {
//        val habitToChange = habitList.find{it.id == habit.id}
//        val habitToChangeIndex = habitList.indexOf(habitToChange)
//        habitList[habitToChangeIndex] = habit
//        habitAdapter.notifyItemChanged(habitToChangeIndex)
//    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putParcelableArrayList(RECYCLER_DATA, habitList as ArrayList<out Parcelable>)
//        super.onSaveInstanceState(outState)
//    }


/**
 * код раньше не работал , но синт ошибок в нем  не было
 * */
//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        val mDataset = savedInstanceState?.getParcelableArrayList<Habit>(RECYCLER_DATA)
//        binding.recyclerView.adapter  = HabitAdapter(this)
//        habitAdapter = binding.recyclerView.adapter as HabitAdapter
//        try {
//            habitList.addAll(mDataset as Collection<Habit>)
//            habitAdapter.addMoreHabits(habitList)
//        }
//        catch (e : Exception) {}
//
//    }



//    override fun onResume() {
//        super.onResume()
//        Log.d(TAG, "List_fragment_onResume")
//    }
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
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d(TAG, "List_fragment_onDestroy")
//    }
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
        private const val RECYCLER_DATA     = "recycler data"
        const val HABIT_TO_EDIT = "habitToEdit"
        private const val BAD_INSTANCE = "BadInstance"

        fun newInstance(bad: Boolean) =
            ListHabitsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(BAD_INSTANCE, bad)
                }
            }
    }

}