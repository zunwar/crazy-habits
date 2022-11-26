package com.example.crazy_habits.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.crazy_habits.*
import com.example.crazy_habits.database.habit.Habit
import com.example.crazy_habits.databinding.FragmentHabitEditBinding
import com.example.crazy_habits.fragments.ColorHabitFragment.Companion.COLOR_HABIT
import com.example.crazy_habits.fragments.ListHabitsFragment.Companion.HABIT_TO_EDIT_ID
import com.example.crazy_habits.viewmodels.HabitEditViewModel
import java.util.*


class HabitEditFragment : Fragment(R.layout.fragment_habit_edit) {

    private var _binding: FragmentHabitEditBinding? = null
    private lateinit var habit: Habit
    private lateinit var oldHabit: Habit
    private val binding get() = _binding!!
    private var edit = false

    private val habitEditViewModel: HabitEditViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.getString(HABIT_TO_EDIT_ID) != null) {
            arguments?.let {
                oldHabit = habitEditViewModel.getHabitToEdit(it.getString(HABIT_TO_EDIT_ID)!!)
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
        binding.radioGroup.check(binding.radioButton0.id)
        binding.prioritySpinner.setSelection(1)
        var colorHabit = -1
        binding.colorOfHabit.background = ShapeColorBox(3, colorHabit)

        if ((arguments != null) && (edit)) {
            with(oldHabit) {
                if (name == getString(R.string.notSpecified)) binding.NameHabitText.setText("")
                else binding.NameHabitText.setText(name)
                binding.DescText.setText(desc)
                when (oldHabit.type) {
                    Type.Good -> binding.radioGroup.check(binding.radioButton0.id)
                    Type.Bad -> binding.radioGroup.check(binding.radioButton1.id)
                }
                when (oldHabit.priority) {
                    Priority.Low    -> binding.prioritySpinner.setSelection(0)
                    Priority.Middle -> binding.prioritySpinner.setSelection(1)
                    Priority.High   -> binding.prioritySpinner.setSelection(2)
                }
                if (number == getString(R.string.empty)) binding.NumberText.setText("")
                else binding.NumberText.setText(number)
                if (period == getString(R.string.empty)) binding.PeriodText.setText("")
                else binding.PeriodText.setText(period)
                binding.colorOfHabit.background = ShapeColorBox(1, oldHabit.colorHabit)
            }
            colorHabit = oldHabit.colorHabit
            binding.addButton.text = getString(R.string.changeButton)
        }

        binding.addButton.setOnClickListener {
            habit = Habit(
                name = binding.NameHabitText.text.toString().ifEmpty { getString(R.string.notSpecified) },
                desc = binding.DescText.text.toString(),
                type =
                when (checkData(view.findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString())) {
                    getString(R.string.goodHabit) -> Type.Good
                    getString(R.string.badHabit) -> Type.Bad
                    else -> {
                        Type.Good
                    }
                },
                priority =
                when (binding.prioritySpinner.selectedItem.toString()) {
                    getString(R.string.highPriority) -> Priority.High
                    getString(R.string.middlePriority) -> Priority.Middle
                    getString(R.string.lowPriority) -> Priority.Low
                    else -> {
                        Priority.Middle
                    }
                },
                number = checkData(binding.NumberText.text.toString()),
                period = checkData(binding.PeriodText.text.toString()),
                colorHabit = colorHabit,
                id = if (edit) oldHabit.id else UUID.randomUUID().toString()
            )
            if (habit.type == Type.Good) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(TAB_ITEM, 0)
            }
            if (habit.type == Type.Bad) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(TAB_ITEM, 1)
            }
//            if (binding.addButton.text == getString(R.string.changeButton)) {
//                findNavController().previousBackStackEntry?.savedStateHandle?.set(MOVE, true)
//            }
            if ((arguments != null) && (edit)) {
                habitEditViewModel.changeHabit(habit)
            } else {
                habitEditViewModel.addHabit(habit)
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    HABIT_ADD,
                    Bundle().apply { putBoolean(ADD_BOOL, true) })
            }
            findNavController().popBackStack()

        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(COLOR_HABIT)
            ?.observe(viewLifecycleOwner) { result ->
                colorHabit = result.getInt(COLOR_HABIT)
                binding.colorOfHabit.background = ShapeColorBox(1, colorHabit)
            }


        binding.chooseColorButton.setOnClickListener {
            if (edit) {
                findNavController().navigate(
                    R.id.action_habitEditFragment_to_colorHabitFragment,
                    Bundle().apply { putParcelable(COLLECTED_HABIT, oldHabit) })
            } else {
                findNavController().navigate(R.id.action_habitEditFragment_to_colorHabitFragment)
            }

        }
    }

    private fun checkData(parameter: String): String {
        return parameter.ifEmpty { getString(R.string.empty) }
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
        const val HABIT_ADD = "habit add"
        const val HABIT_CHANGE = "habit change"
        const val ADD_BOOL = "habit change"
        const val TAB_ITEM = "tab item"
        const val MOVE = "move item"

//        fun newInstance(bad: Boolean) =
//            HabitEditFragment().apply {
//                arguments = Bundle().apply {
//                    putBoolean(COLLECTED_HABIT, true)
//                }
//            }
    }

}