package com.example.presentation.edithabit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.entities.Habit
import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import com.example.presentation.R
import com.example.presentation.colorchoose.ColorHabitFragment.Companion.COLOR_HABIT
import com.example.presentation.databinding.FragmentHabitEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HabitEditFragment : Fragment(R.layout.fragment_habit_edit) {

    private var _binding: FragmentHabitEditBinding? = null
    private val binding get() = _binding!!
    private val habitEditViewModel: HabitEditViewModel by viewModels()

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
        habitEditViewModel.displayOldHabit().observe(viewLifecycleOwner) {
            displayOldHabit(it)
        }

        //отображение ответа сервера, скрывание прогресс бара и закрытие фрагмента
        habitEditViewModel.closeFragment.observe(viewLifecycleOwner) {
            Toast.makeText(
                activity,
                "Server response: ${habitEditViewModel.serverResponse}",
                Toast.LENGTH_LONG
            ).show()
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
        try {
            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(COLOR_HABIT)
                ?.observe(viewLifecycleOwner) { result ->
                    habitEditViewModel.setColorFromColorFragment(result.getInt(COLOR_HABIT))
                    binding.colorOfHabit.background = ShapeColorBox(1, habitEditViewModel.colorHabit)
                }
        } finally {}

//подписка на изменение полей и включение или отключение кнопки сохранения/изменения
        habitEditViewModel.uiState.observe(viewLifecycleOwner) {
            if (it.isErrorName) binding.NameHabitLayout.error = getString(R.string.requiredToFill)
            else binding.NameHabitLayout.error = null
            if (it.isErrorDesc) binding.Desc.error = getString(R.string.requiredToFill)
            else binding.Desc.error = null
            if (it.isErrorNumber) binding.Number.error = getString(R.string.requiredToFill)
            else binding.Number.error = null
            if (it.isErrorFrequency) binding.FrequencyLayout.error = getString(R.string.requiredToFill)
            else binding.FrequencyLayout.error = null

            if (it.isErrorName || it.isErrorDesc || it.isErrorNumber || it.isErrorFrequency) {
                binding.addButton.isClickable = false
                binding.addButton.isEnabled = false
            } else {
                binding.addButton.isClickable = true
                binding.addButton.isEnabled = true
            }
        }

        observeTextFields()
        checkDefaultRadioGroupValue()
        chooseColorButton()
        submitButton()
    }

    private fun observeTextFields() {
        binding.NameHabitText.doOnTextChanged { text, _, _, _ ->
            habitEditViewModel.setNameAndValidate(
                text.toString()
            )
        }
        binding.DescText.doOnTextChanged { text, _, _, _ ->
            habitEditViewModel.setDescAndValidate(
                text.toString()
            )
        }
        binding.Number.doOnTextChanged { text, _, _, _ ->
            habitEditViewModel.setNumberAndValidate(
                text.toString()
            )
        }
        binding.Frequency.doOnTextChanged { text, _, _, _ ->
            habitEditViewModel.setFrequencyAndValidate(
                text.toString()
            )
        }
        binding.radioGroup.setOnCheckedChangeListener { _, i ->
            val type = getRadioButton(i)
            habitEditViewModel.setType(type)
        }
        initPrioritySpinner()
    }

    private fun checkDefaultRadioGroupValue() {
        binding.radioGroup.check(binding.radioButton0.id)
    }

    private fun chooseColorButton() {
        binding.chooseColorButton.setOnClickListener {
            habitEditViewModel.toColorFragmentClicked()
        }
    }

    private fun submitButton() {
        binding.addButton.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                EDIT_BOOL,
                habitEditViewModel.isEditable
            )
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                TAB_ITEM,
                habitEditViewModel.setRightTabItem(habitEditViewModel.uiState.value!!.type!!)
            )
            binding.progressBar.visibility = View.VISIBLE
            habitEditViewModel.saveHabit()
        }
    }

    private fun displayOldHabit(oldHabit: Habit) {
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
        binding.prioritySpinner.setSelection(1)
        binding.prioritySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    habitEditViewModel.setPriority(parent!!.adapter.getItem(position) as Priority)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getRadioButton(idButton: Int): Type {
        return when (requireView().findViewById<RadioButton>(idButton).text.toString()) {
            getString(R.string.goodHabit) -> Type.Good
            getString(R.string.badHabit) -> Type.Bad
            else -> Type.Good
        }
    }

    companion object {
        const val COLLECTED_HABIT = "collectedHabit"
        const val EDIT_BOOL = "habit change"
        const val TAB_ITEM = "tab item"
    }

}