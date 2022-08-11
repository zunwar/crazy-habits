package com.example.crazy_habits.fragments

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.Habit
import com.example.crazy_habits.adapters.HabitAdapter
import com.example.crazy_habits.R
import com.example.crazy_habits.databinding.FragmentListHabitsBinding
import com.example.crazy_habits.fragments.HabitEditFragment.Companion.COLLECTED_HABIT
import kotlin.Exception

class ListHabitsFragment : Fragment(R.layout.fragment_list_habits), HabitAdapter.OnItemClickListener {


    private var _binding: FragmentListHabitsBinding? = null
    private val habitList : MutableList<Habit> = mutableListOf()
    private lateinit var habit : Habit
    private lateinit var habitAdapter : HabitAdapter
    private var idHabitToEdit : String = ""
    private var edit = false


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener("frag2_AddButton", this, FragmentResultListener(
            fun (requstKey : String , bundle : Bundle) {
                edit = true
                habit = bundle.getParcelable<Habit>(COLLECTED_HABIT)!!
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
        _binding = FragmentListHabitsBinding.inflate(inflater, container, false)
        Log.d(TAG, "list_frag1_onCreateView")
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "list_frag1_onViewCreated")
        binding.FAB.setOnClickListener {
            val result = Bundle()
            result.putString("df1", "clicked")
            parentFragmentManager.setFragmentResult("frag1_addNewHabitButton", result)
        }





    }
    override fun onItemClicked(id: String) {
//        habitList.remove(habitList.find { it.id == id })
        idHabitToEdit = id
        val bundle = Bundle ()
        bundle.putParcelable(HABIT_TO_EDIT, habitList.find { it.id == idHabitToEdit })
        parentFragmentManager.setFragmentResult(HABIT_TO_EDIT, bundle)

//        habitAdapter.notifyItemRemoved(habitList.indexOf(habitList.find { it.id == id }))
//        habitAdapter.notifyItemChanged(habitList.indexOf(habitList.find { it.id == id }))
    }


    private fun  initRecyclerView () {
        Log.d(TAG, "list_frag1_initRecyclerView")
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



    override fun onResume() {
        super.onResume()
        Log.d(TAG, "list_frag1_onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "list_frag1_onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "list_frag1_onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "list_frag1_onDestroy")
//        dummyButton = null
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "list_frag1_onDetach")
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
        Log.d(TAG, "list_frag1_onDestroyView")
        _binding = null
    }
}