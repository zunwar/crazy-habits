package com.example.crazy_habits.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.navigation.fragment.findNavController
import com.example.crazy_habits.*
import com.example.crazy_habits.databinding.FragmentHabitEditBinding
import com.example.crazy_habits.fragments.ColorHabitFragment.Companion.COLOR_HABIT
import com.example.crazy_habits.fragments.GoodHabitsFragment.Companion.HABIT_TO_EDIT
import com.example.crazy_habits.fragments.ViewPagerFragment.Companion.FAB_BUTTON_CLICK
import java.util.*


class HabitEditFragment : Fragment(R.layout.fragment_habit_edit) {

    private var _binding: FragmentHabitEditBinding? = null
    private lateinit var habit : Habit
    private lateinit var oldHabit: Habit
    private val binding get() = _binding!!
    private var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.getParcelable<Habit>(HABIT_TO_EDIT) != null ) {
            arguments?.let {
                oldHabit = it.getParcelable<Habit>(HABIT_TO_EDIT)!!
            }
            edit = true
        }





    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                binding.radioGroup.check(binding.radioButton1.id)
        var colorHabit = -1
//        Log.d(TAG, "edit_frag_onViewCreated")
        binding.colorOfHabit.background = ShapeColorBox(3, colorHabit)

        if ((arguments != null) && (edit)) {
            with (oldHabit) {
                if (name == getString(R.string.notSet)) binding.NameHabitText.setText("") else  binding.NameHabitText.setText(name)
                binding.DescText.setText(desc)
                when (oldHabit.type) {
                    getString(R.string.goodHabit)   -> binding.radioGroup.check(binding.radioButton1.id)
                    getString(R.string.badHabit)     -> binding.radioGroup.check(binding.radioButton3.id)
                }
                when (oldHabit.priority) {
                    getString(R.string.empty) -> binding.prioritySpinner.setSelection(0)
                    getString(R.string.highPriority) -> binding.prioritySpinner.setSelection(1)
                    getString(R.string.middlePriority) -> binding.prioritySpinner.setSelection(2)
                    getString(R.string.lowPriority) -> binding.prioritySpinner.setSelection(3)
                }
                if (number == getString(R.string.empty)) binding.NumberText.setText("") else binding.NumberText.setText(number)
                if (period == getString(R.string.empty)) binding.PeriodText.setText("") else binding.PeriodText.setText(period)
                binding.colorOfHabit.background = ShapeColorBox(1, oldHabit.colorHabit)
            }
            binding.addButton.text = getString(R.string.changeButton)
        }

        binding.addButton.setOnClickListener {
            habit = Habit(
                name       = binding.NameHabitText.text.toString(),
                desc       = binding.DescText.text.toString(),
                type       = view.findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString(),
                priority   = binding.prioritySpinner.selectedItem.toString(),
                number     = binding.NumberText.text.toString(),
                period     = binding.PeriodText.text.toString(),
                colorHabit = colorHabit,
                id         = UUID.randomUUID().toString()
            )
            val result = Bundle().apply {
                putParcelable(COLLECTED_HABIT, habit)
            }
            if (habit.type == getString(R.string.goodHabit)){

                findNavController().previousBackStackEntry?.savedStateHandle?.set(GOOD_HABIT_ADD, result)
                findNavController().previousBackStackEntry?.savedStateHandle?.set(TAB_ITEM, 0)

            }
            if (habit.type == getString(R.string.badHabit)){
                findNavController().previousBackStackEntry?.savedStateHandle?.set(BAD_HABIT_ADD, result)
                findNavController().previousBackStackEntry?.savedStateHandle?.set(TAB_ITEM, 1)
            }
//            if (binding.addButton.text == getString(R.string.changeButton)) {
//                findNavController().previousBackStackEntry?.savedStateHandle?.set(MOVE, true)
//            }
            findNavController().popBackStack()

        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(COLOR_HABIT)?.observe(viewLifecycleOwner) {result ->
            colorHabit = result.getInt(COLOR_HABIT)
            binding.colorOfHabit.background = ShapeColorBox(1, colorHabit)
        }


        binding.chooseColorButton.setOnClickListener {
            findNavController().navigate(R.id.action_habitEditFragment_to_colorHabitFragment)
        }

    }

//    override fun onResume() {
//        super.onResume()
//        Log.d(TAG, "edit_frag_onResume")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d(TAG, "edit_frag_onPause")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d(TAG, "edit_frag_onStop")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d(TAG, "edit_frag_onDestroy")
//    }

    override fun onDestroyView() {
        super.onDestroyView()
//        Log.d(TAG, "edit_frag_onDestroyView")
        _binding = null
    }

//    override fun onDetach() {
//        super.onDetach()
//        Log.d(TAG, "edit_frag_onDetach")
//    }


    companion object {
        const val COLLECTED_HABIT = "collectedHabit"
        const val GOOD_HABIT_ADD = "good habit add"
        const val BAD_HABIT_ADD = "bad habit add"
        const val TAB_ITEM = "tab item"
        const val MOVE = "move item"
    }

    fun Fragment.getNavigationResult(key: String = "result") =
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(key)

    fun Fragment.setNavigationResult(key: String = "result", result: Bundle ) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
    }
}