package com.example.crazy_habits.fragments

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.Habit
import com.example.crazy_habits.adapters.HabitAdapter
import com.example.crazy_habits.R
import com.example.crazy_habits.databinding.FragmentGoodHabitsBinding
import com.example.crazy_habits.fragments.HabitEditFragment.Companion.COLLECTED_HABIT
import com.example.crazy_habits.fragments.HabitEditFragment.Companion.GOOD_HABIT_ADD
import kotlin.Exception

class GoodHabitsFragment : Fragment(R.layout.fragment_good_habits), HabitAdapter.OnItemClickListener {


    private var _binding: FragmentGoodHabitsBinding? = null
    private val habitList : MutableList<Habit> = mutableListOf()
    private lateinit var habit : Habit
    private lateinit var habitAdapter : HabitAdapter
    private var idHabitToEdit : String = ""
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "good_frag_onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoodHabitsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(GOOD_HABIT_ADD)?.observe(viewLifecycleOwner) {result ->
            habit = result.getParcelable<Habit>(COLLECTED_HABIT)!!
            initRecyclerView()
        }
    }


    override fun onItemClicked(id: String) {
        idHabitToEdit = id
        val bundle = Bundle ()
        bundle.putParcelable(HABIT_TO_EDIT, habitList.find { it.id == idHabitToEdit })
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
        Log.d(TAG, "good_frag_initRecyclerView")
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter  = HabitAdapter(this)
        habitAdapter = binding.recyclerView.adapter as HabitAdapter
        if ((habitList.find{it.id == idHabitToEdit}) != null) {
            changeHabit(idHabitToEdit)
            idHabitToEdit = ""
        } else habitList.add(habit)
        habitAdapter.addMoreHabits(habitList)
        binding.recyclerView.scrollToPosition(habitAdapter.itemCount-1)
    }


    private fun changeHabit (idHabitToChange : String) {
        val habitToChange = habitList.find{it.id == idHabitToChange}!!
        with (habitToChange) {
            name       = habit.name
            desc       = habit.desc
            type       = habit.type
            priority   = habit.priority
            number     = habit.number
            period     = habit.period
            colorHabit = habit.colorHabit
        }
        habitAdapter.notifyItemChanged(habitList.indexOf(habitToChange))

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(RECYCLER_DATA, habitList as ArrayList<out Parcelable>)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val mDataset = savedInstanceState?.getParcelableArrayList<Habit>(RECYCLER_DATA)
        binding.recyclerView.adapter  = HabitAdapter(this)
        habitAdapter = binding.recyclerView.adapter as HabitAdapter
        try {
            habitList.addAll(mDataset as Collection<Habit>)
            habitAdapter.addMoreHabits(habitList)
        }
        catch (e : Exception) {}

    }



//    override fun onResume() {
//        super.onResume()
//        Log.d(TAG, "good_fragment_onResume")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d(TAG, "good_fragment_onPause")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d(TAG, "good_fragment_onStop")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d(TAG, "good_fragment_onDestroy")
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        Log.d(TAG, "good_fragment_onDetach")
//    }

    override fun onDestroyView() {
        super.onDestroyView()
//        Log.d(TAG, "good_fragment_onDestroyView")
        _binding = null
    }

    companion object {
        /**
         *
         *
         */
        private const val RECYCLER_DATA     = "recycler data"
        const val HABIT_TO_EDIT = "habitToEdit"
    }

}