package com.example.presentation.listhabits

import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.presentation.databinding.BottomSheetBinding
import com.example.presentation.R.string
import com.example.theme.R.style
import com.example.theme.R.color
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    private val listHabitsViewModel: ListHabitsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, style.AppBottomSheetDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior = (dialog as BottomSheetDialog).behavior
        bottomSheetBehavior.peekHeight = 40.dpToPx
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterButton.setOnClickListener {
            listHabitsViewModel.updateNameToFilter(binding.filterTextSet.text.toString())
        }

        binding.bottomSheetRoot.setOnClickListener {
            when (bottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_COLLAPSED -> bottomSheetBehavior.state =
                    BottomSheetBehavior.STATE_EXPANDED
                BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior.state =
                    BottomSheetBehavior.STATE_COLLAPSED
                BottomSheetBehavior.STATE_DRAGGING -> {}
                BottomSheetBehavior.STATE_SETTLING -> {}
                BottomSheetBehavior.STATE_HALF_EXPANDED -> {}
                BottomSheetBehavior.STATE_HIDDEN -> {}
            }
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.filterButton.background.setTint(
                        ContextCompat.getColor(
                            context!!,
                            color.gray
                        )
                    )
                } else {
                    binding.filterButton.background.setTint(
                        ContextCompat.getColor(
                            context!!,
                            color.purple_500
                        )
                    )
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 25) Toast.makeText(
                    requireContext(),
                    getString(string.too_much),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.filterTextSet.addTextChangedListener(textWatcher)
    }

    companion object {
        const val BottomSheet_TAG = "BottomSheet"
    }

    val Int.dpToPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

}