package com.example.crazy_habits.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.crazy_habits.*
import com.example.crazy_habits.adapters.CustomSpinnerAdapter
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.databinding.FragmentHabitEditBinding
import com.example.crazy_habits.fragments.ColorHabitFragment.Companion.COLOR_HABIT
import com.example.crazy_habits.fragments.ListHabitsFragment.Companion.HABIT_TO_EDIT_ID
import com.example.crazy_habits.viewmodels.HabitEditViewModel


class HabitEditFragment : Fragment(R.layout.fragment_habit_edit) {

    private var _binding: FragmentHabitEditBinding? = null
    private val binding get() = _binding!!
    private val habitEditViewModel: HabitEditViewModel by viewModels {HabitEditViewModel.Factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        initPrioritySpinner()
        if (!requireArguments().isEmpty) {
            arguments?.let {
                val liveDataOldHabit = habitEditViewModel.getHabitToEdit(it.getString(HABIT_TO_EDIT_ID)!!)
                liveDataOldHabit.observe(viewLifecycleOwner){oldHabit ->
                    displayEditableHabit(oldHabit)
                }
            }
        }
        binding.colorOfHabit.background = ShapeColorBox(3, habitEditViewModel.colorHabit)
        collectHabitOnSubmitButtonPress()


        habitEditViewModel.closeFragment.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        habitEditViewModel.navigateToColorFragmentWithBundle.observe(viewLifecycleOwner) {
            findNavController().navigate(
                R.id.action_habitEditFragment_to_colorHabitFragment,
//                    Bundle().apply { putParcelable(COLLECTED_HABIT, habitEditViewModel.getHabitToEdit(requireArguments().getString(HABIT_TO_EDIT_ID)!!)) }
            )
        }
        habitEditViewModel.navigateToColorFragment.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_habitEditFragment_to_colorHabitFragment)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(COLOR_HABIT)
            ?.observe(viewLifecycleOwner) { result ->
                habitEditViewModel.setColorOfHabit(result.getInt(COLOR_HABIT))
                binding.colorOfHabit.background = ShapeColorBox(1, habitEditViewModel.colorHabit)
            }

        binding.chooseColorButton.setOnClickListener {
            habitEditViewModel.toColorFragmentClicked()
        }
    }

    private fun collectHabitOnSubmitButtonPress() {
        binding.addButton.setOnClickListener {
            val habit = HabitEntity(
                name = binding.NameHabitText.text.toString().ifEmpty { getString(R.string.notSpecified) },
                desc = binding.DescText.text.toString(),
                type =
                when (checkData(requireView().findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString())) {
                    getString(R.string.goodHabit) -> Type.Good
                    getString(R.string.badHabit) -> Type.Bad
                    else -> {
                        Type.Good
                    }
                },
                priority = habitEditViewModel.selectedPriority,
                number = checkData(binding.NumberText.text.toString()),
                period = checkData(binding.PeriodText.text.toString()),
                colorHabit = habitEditViewModel.colorHabit,
                id = habitEditViewModel.getId()
            )
            if (habit.type == Type.Good) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(TAB_ITEM, 0)
            }
            if (habit.type == Type.Bad) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(TAB_ITEM, 1)
            }
            saveAndCloseScreen(habit)
        }
    }

    private fun displayEditableHabit(oldHabit: HabitEntity) {
        if (habitEditViewModel.isEditable) {
            with(oldHabit) {
                if (name == getString(R.string.notSpecified)) binding.NameHabitText.setText("")
                else binding.NameHabitText.setText(name)
                binding.DescText.setText(desc)
                when (this.type) {
                    Type.Good -> binding.radioGroup.check(binding.radioButton0.id)
                    Type.Bad -> binding.radioGroup.check(binding.radioButton1.id)
                }
                when (this.priority) {
                    Priority.High    -> binding.prioritySpinner.setSelection(0)
                    Priority.Middle -> binding.prioritySpinner.setSelection(1)
                    Priority.Low   -> binding.prioritySpinner.setSelection(2)
                }
                if (number == getString(R.string.empty)) binding.NumberText.setText("")
                else binding.NumberText.setText(number)
                if (period == getString(R.string.empty)) binding.PeriodText.setText("")
                else binding.PeriodText.setText(period)
                binding.colorOfHabit.background = ShapeColorBox(1, this.colorHabit)
            }
            habitEditViewModel.setColorOfHabit(oldHabit.colorHabit)
            binding.addButton.text = getString(R.string.changeButton)
        }
    }

    private fun saveAndCloseScreen(habit: HabitEntity) {
        if (habitEditViewModel.isEditable) {
            habitEditViewModel.changeHabit(habit)
        } else {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                HABIT_ADD,
                Bundle().apply { putBoolean(ADD_BOOL, true) })
            habitEditViewModel.addHabit(habit)
        }
    }

    private fun checkData(parameter: String): String {
        return parameter.ifEmpty { getString(R.string.empty) }
    }

    private fun initPrioritySpinner() {
        val csa = CustomSpinnerAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            Priority.values()
        )
        binding.prioritySpinner.adapter = csa
        binding.prioritySpinner.setSelection(habitEditViewModel.selectedPriority.id)
        binding.prioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                habitEditViewModel.selectPriority(parent!!.adapter.getItem(position) as Priority)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    companion object {
        const val COLLECTED_HABIT = "collectedHabit"
        const val HABIT_ADD = "habit add"
        const val ADD_BOOL = "habit change"
        const val TAB_ITEM = "tab item"

//        fun newInstance(bad: Boolean) =
//            HabitEditFragment().apply {
//                arguments = Bundle().apply {
//                    putBoolean(COLLECTED_HABIT, true)
//                }
//            }
    }

}