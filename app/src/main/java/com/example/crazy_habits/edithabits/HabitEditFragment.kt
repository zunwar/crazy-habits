package com.example.crazy_habits.edithabits

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
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.databinding.FragmentHabitEditBinding
import com.example.crazy_habits.colorchoose.ColorHabitFragment.Companion.COLOR_HABIT
import com.example.crazy_habits.utils.Priority
import com.example.crazy_habits.utils.ShapeColorBox
import com.example.crazy_habits.utils.Type


class HabitEditFragment : Fragment(R.layout.fragment_habit_edit) {

    private var _binding: FragmentHabitEditBinding? = null
    private val binding get() = _binding!!
    private val habitEditViewModel: HabitEditViewModel by viewModels { HabitEditViewModel.Factory }

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

//отображаем старую/редактируемую привычку, если возможно
        habitEditViewModel.displayOldHabit.observe(viewLifecycleOwner){
            displayOldHabit(it)
        }

//закрытие фрагмента
        habitEditViewModel.closeFragment.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

//подписываемся на нажатие кнопки перехода на фрагмент с выбором цвета привычки
        habitEditViewModel.navigateToColorFragment.observe(viewLifecycleOwner) {
            findNavController().navigate(
                R.id.action_habitEditFragment_to_colorHabitFragment,
                Bundle().apply {
                    putString(
                        COLLECTED_HABIT,
                        habitEditViewModel.getId()
                    )
                }
            )
        }

//получение результата с ColorFragment и сохранение его в EditViewModel и задание цвета строки,
// показывающей выбранный ранее цвет
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(COLOR_HABIT)
            ?.observe(viewLifecycleOwner) { result ->
                habitEditViewModel.setColorFromColorFragment(result.getInt(COLOR_HABIT))
                binding.colorOfHabit.background = ShapeColorBox(1, habitEditViewModel.colorHabit)
            }

        checkDefaultRadioGroupValue()
        initPrioritySpinner()
        chooseColorButton()
        submitButton()
    }

    private fun checkDefaultRadioGroupValue() {
        binding.radioGroup.check(binding.radioButton0.id)
    }

    private fun chooseColorButton() {
        binding.chooseColorButton.setOnClickListener {
            habitEditViewModel.toColorFragmentClicked()
        }
    }

    private fun collectAndSaveHabit() {
        val habit = HabitEntity(
            name = binding.NameHabitText.text.toString()
                .ifEmpty { getString(R.string.notSpecified) },
            desc = binding.DescText.text.toString(),
            type =
            when (requireView().findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString()) {
                getString(R.string.goodHabit) -> Type.Good
                getString(R.string.badHabit) -> Type.Bad
                else -> Type.Good
            },
            priority = habitEditViewModel.selectedPriority,
            number = checkData(binding.NumberText.text.toString()),
            period = checkData(binding.PeriodText.text.toString()),
            colorHabit = habitEditViewModel.colorHabit,
            id = habitEditViewModel.getId()
        )
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            EDIT_BOOL,
            habitEditViewModel.isEditable)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            TAB_ITEM,
            habitEditViewModel.setRightTabItem(habit.type)
        )
        habitEditViewModel.saveHabit(habit)
    }

    private fun submitButton() {
        binding.addButton.setOnClickListener {
            collectAndSaveHabit()
        }
    }

    private fun displayOldHabit(oldHabit: HabitEntity) {
                with(oldHabit) {
                    if (name == getString(R.string.notSpecified)) binding.NameHabitText.setText("")
                    else binding.NameHabitText.setText(name)
                    binding.DescText.setText(desc)
                    when (this.type) {
                        Type.Good -> binding.radioGroup.check(binding.radioButton0.id)
                        Type.Bad -> binding.radioGroup.check(binding.radioButton1.id)
                    }
                    when (this.priority) {
                        Priority.High -> binding.prioritySpinner.setSelection(0)
                        Priority.Middle -> binding.prioritySpinner.setSelection(1)
                        Priority.Low -> binding.prioritySpinner.setSelection(2)
                    }
                    if (number == getString(R.string.empty)) binding.NumberText.setText("")
                    else binding.NumberText.setText(number)
                    if (period == getString(R.string.empty)) binding.PeriodText.setText("")
                    else binding.PeriodText.setText(period)
                }
                habitEditViewModel.setColorOfHabit(oldHabit.colorHabit)
                binding.colorOfHabit.background = ShapeColorBox(1, habitEditViewModel.colorHabit)
                binding.addButton.text = getString(R.string.changeButton)
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
        binding.prioritySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
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
        const val EDIT_BOOL = "habit change"
        const val TAB_ITEM = "tab item"
    }

}