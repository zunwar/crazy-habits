package com.example.crazy_habits.edithabits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.crazy_habits.*
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.databinding.FragmentHabitEditBinding
import com.example.crazy_habits.colorchoose.ColorHabitFragment.Companion.COLOR_HABIT
import com.example.crazy_habits.database.habit.DataOfHabit
import com.example.crazy_habits.listhabits.ListHabitsFragment.Companion.HABIT_TO_EDIT_ID
import com.example.crazy_habits.utils.Priority
import com.example.crazy_habits.utils.ShapeColorBox
import com.example.crazy_habits.utils.Type
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class HabitEditFragment : Fragment(R.layout.fragment_habit_edit) {

    private var _binding: FragmentHabitEditBinding? = null
    private val binding get() = _binding!!
    private val habitEditViewModel: HabitEditViewModel by viewModels {
        HabitEditViewModel.provideFactory(
            arguments?.getString(HABIT_TO_EDIT_ID)
        )
    }

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
        binding.progressBar.visibility = View.GONE

//отображаем старую/редактируемую привычку, если возможно
        habitEditViewModel.displayOldHabit.observe(viewLifecycleOwner) {
            displayOldHabit(it)
        }

//отображение ответа сервера, скрывание прогресс бара и закрытие фрагмента
        habitEditViewModel.closeFragment.observe(viewLifecycleOwner) {
            val serverResponse = habitEditViewModel.getServerResponse()
            Toast.makeText(activity, "Server response: $serverResponse", Toast.LENGTH_LONG).show()
            binding.progressBar.visibility = View.GONE
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
        val formatter = DateTimeFormatter.ofPattern("ddHHmss")
        val currentDateTimeInt = LocalDateTime.now(ZoneId.of("UTC+5")).format(formatter).toInt()
        val dataOfHabit = DataOfHabit(
            name = binding.NameHabitText.text.toString(),
            desc = binding.DescText.text.toString(),
            type =
            when (requireView().findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString()) {
                getString(R.string.goodHabit) -> Type.Good
                getString(R.string.badHabit) -> Type.Bad
                else -> Type.Good
            },
            priority = habitEditViewModel.selectedPriority,
            number = binding.Number.text.toString().toInt(),
            frequency = binding.Frequency.text.toString().toInt(),
            colorHabit = habitEditViewModel.colorHabit,
            date = currentDateTimeInt
        )
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            EDIT_BOOL,
            habitEditViewModel.isEditable)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            TAB_ITEM,
            habitEditViewModel.setRightTabItem(dataOfHabit.type)
        )
        habitEditViewModel.saveHabit(dataOfHabit)
    }

    private fun submitButton() {
        binding.addButton.setOnClickListener {
            if (validateView()) {
                binding.addButton.isClickable = false
                binding.addButton.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
                collectAndSaveHabit()
            }
        }
    }

    private fun displayOldHabit(oldHabit: HabitEntity) {
        with(oldHabit) {
            binding.NameHabitText.setText(name)
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
            binding.Number.setText(number.toString())
            binding.Frequency.setText(frequency.toString())
        }
        habitEditViewModel.setColorOfHabit(oldHabit.colorHabit)
        binding.colorOfHabit.background = ShapeColorBox(1, habitEditViewModel.colorHabit)
        binding.addButton.text = getString(R.string.changeButton)
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

// проверка полей на заполненность, после клика на кнопку добавить/изменить
    private fun validateView(): Boolean {
        val isEmptyViews: MutableList<Boolean> = mutableListOf()

        val validate = { viewLayout: TextInputLayout, view: TextInputEditText ->
            if (view.text.isNullOrEmpty()) {
                isEmptyViews.add(view.text.isNullOrEmpty())
                viewLayout.error = getString(R.string.requiredToFill)
            } else {
                viewLayout.error = null
            }
        }
        validate(binding.NameHabitLayout, binding.NameHabitText)
        validate(binding.Desc, binding.DescText)
        validate(binding.NumberLayout, binding.Number)
        validate(binding.FrequencyLayout, binding.Frequency)

        return !isEmptyViews.contains(true)
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