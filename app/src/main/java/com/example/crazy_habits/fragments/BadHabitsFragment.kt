package com.example.crazy_habits.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.Habit
import com.example.crazy_habits.R
import com.example.crazy_habits.adapters.HabitAdapter
import com.example.crazy_habits.databinding.FragmentBadHabitsBinding
import com.example.crazy_habits.fragments.HabitEditFragment.Companion.BAD_HABIT_ADD


class BadHabitsFragment : Fragment(R.layout.fragment_bad_habits), HabitAdapter.OnItemClickListener {



    private var _binding: FragmentBadHabitsBinding? = null
    private val habitList : MutableList<Habit> = mutableListOf()
    private lateinit var habit : Habit
    private lateinit var habitAdapter : HabitAdapter
    private var idHabitToEdit : String = ""
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "bad_fragment_onCreate")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBadHabitsBinding.inflate(inflater, container, false)
        Log.d(TAG, "bad_fragment_onCreateView")
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "bad_fragment_onViewCreated")
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(BAD_HABIT_ADD)?.observe(viewLifecycleOwner) {result ->
            habit = result.getParcelable<Habit>(HabitEditFragment.COLLECTED_HABIT)!!
            initRecyclerView()
        }
    }

    override fun onItemClicked(id: String) {
        idHabitToEdit = id
        val bundle = Bundle ()
        bundle.putParcelable(GoodHabitsFragment.HABIT_TO_EDIT, habitList.find { it.id == idHabitToEdit })
        findNavController().navigate(R.id.action_viewPagerFragment_to_habitEditFragment, bundle)
    }


    private fun  initRecyclerView () {
        binding.recyclerView2.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView2.adapter  = HabitAdapter(this)
        habitAdapter = binding.recyclerView2.adapter as HabitAdapter
        if ((habitList.find{it.id == idHabitToEdit}) != null) {
            changeHabit(idHabitToEdit)
            idHabitToEdit = ""
        } else habitList.add(habit)
        habitAdapter.addMoreHabits(habitList)
        binding.recyclerView2.scrollToPosition(habitAdapter.itemCount-1)
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

    companion object {

    }



    override fun onResume() {
        super.onResume()
        Log.d(TAG, "bad_fragment_onResume")

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "bad_fragment_onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "bad_fragment_onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "bad_fragment_onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "bad_fragment_onDetach")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}