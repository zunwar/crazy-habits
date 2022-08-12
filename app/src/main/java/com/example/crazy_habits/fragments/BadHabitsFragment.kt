package com.example.crazy_habits.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crazy_habits.Habit
import com.example.crazy_habits.R
import com.example.crazy_habits.adapters.HabitAdapter
import com.example.crazy_habits.databinding.FragmentBadHabitsBinding


class BadHabitsFragment : Fragment(R.layout.fragment_bad_habits), HabitAdapter.OnItemClickListener {



    private var _binding: FragmentBadHabitsBinding? = null
    private val habitList : MutableList<Habit> = mutableListOf()
    private lateinit var habit : Habit
    private lateinit var habitAdapter : HabitAdapter
    private var idHabitToEdit : String = ""
    private var edit = false
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener("frag2_AddButton_bad", this, FragmentResultListener(
            fun (requstKey : String , bundle : Bundle) {
                edit = true
                habit = bundle.getParcelable<Habit>(HabitEditFragment.COLLECTED_HABIT)!!
                parentFragmentManager.popBackStack()
                initRecyclerView ()
            }
        ))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBadHabitsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onItemClicked(id: String) {
//        habitList.remove(habitList.find { it.id == id })
        idHabitToEdit = id
        val bundle = Bundle ()
        bundle.putParcelable(GoodHabitsFragment.HABIT_TO_EDIT, habitList.find { it.id == idHabitToEdit })
        parentFragmentManager.setFragmentResult(GoodHabitsFragment.HABIT_TO_EDIT, bundle)

//        habitAdapter.notifyItemRemoved(habitList.indexOf(habitList.find { it.id == id }))
//        habitAdapter.notifyItemChanged(habitList.indexOf(habitList.find { it.id == id }))
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
        /**
         *
         *
         */
        private const val RECYCLER_DATA     = "recycler data"
        const val HABIT_TO_EDIT = "habitToEdit"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}