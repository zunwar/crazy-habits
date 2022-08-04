package com.example.crazy_habits.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.fragment.app.FragmentResultListener
import com.example.crazy_habits.*
import com.example.crazy_habits.databinding.ActivitySecondBinding
import com.example.crazy_habits.databinding.FragmentHabitEditBinding
import com.example.crazy_habits.fragments.ListHabitsFragment.Companion.HABIT_TO_EDIT
import java.util.*
import kotlin.math.log

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "errorqwer"
private const val COLOR_HABIT = "colorHabit"
const val COLLECTED_HABIT = "collectedHabit"





class HabitEditFragment : Fragment(R.layout.fragment_habit_edit) {

    private var _binding: FragmentHabitEditBinding? = null
    lateinit var habit : Habit
    var editedit : Boolean = false
    private lateinit var oldHabit: Habit
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
//    private var param1: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            oldHabit = it.getParcelable<Habit>(HABIT_TO_EDIT)!!
//            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHabitEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        binding.radioGroup.check(binding.radioButton2.id)
        var type = ""
        var colorHabit = -1
        var edit = false
        Log.d(TAG, "edit_frag2_onViewCreated")
        binding.colorOfHabit.background = ShapeColorBox(1, colorHabit)

        if (arguments != null) {
            with (oldHabit) {
                if (name == getString(R.string.notSet)) binding.NameHabitText.setText("") else  binding.NameHabitText.setText(name)
                binding.DescText.setText(desc)
                when (oldHabit.type) {
                    getString(R.string.goodHabit)   -> binding.radioGroup.check(binding.radioButton1.id)
                    getString(R.string.neutralHabit) -> binding.radioGroup.check(binding.radioButton2.id)
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
            if (binding.radioGroup.checkedRadioButtonId != -1 ) edit = true
            editedit = true

        }

        binding.radioGroup.forEach {
            it.setOnClickListener{
                type = view.findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString()
            }
        }

        binding.addButton.setOnClickListener {
            habit = Habit(
                name       = binding.NameHabitText.text.toString(),
                desc       = binding.DescText.text.toString(),
                type       = if (edit) (view.findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString()) else type,
                priority   = binding.prioritySpinner.selectedItem.toString(),
                number     = binding.NumberText.text.toString(),
                period     = binding.PeriodText.text.toString(),
                colorHabit = colorHabit,
                id         = UUID.randomUUID().toString()
            )
            val result = Bundle().apply {
                putParcelable(COLLECTED_HABIT, habit)
            }
            parentFragmentManager.setFragmentResult("frag2_AddButton", result)
        }


        parentFragmentManager.setFragmentResultListener("frag3_okButton", this, FragmentResultListener(
            fun (requstKey : String , bundle : Bundle) {
                colorHabit = bundle.getInt(ColorHabitFragment.COLOR_HABIT)
                binding.colorOfHabit.background = ShapeColorBox(1, colorHabit)
            }
        ))


        binding.chooseColorButton.setOnClickListener {
            parentFragmentManager.setFragmentResult("frag2_chooseColorButton", Bundle())
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "edit_frag2_onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "edit_frag2_onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "edit_frag2_onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "edit_frag2_onDestroy")
//        dummyButton = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "edit_frag2_onDestroyView")
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "edit_frag2_onDetach")
    }


    companion object {
    }
}