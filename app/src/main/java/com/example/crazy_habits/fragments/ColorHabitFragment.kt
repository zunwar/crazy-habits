package com.example.crazy_habits.fragments

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.doOnLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.crazy_habits.FirstActivity.Companion.TAG
import com.example.crazy_habits.Habit
import com.example.crazy_habits.ShapeColorBox
import com.example.crazy_habits.databinding.FragmentColorHabitBinding
import com.example.crazy_habits.fragments.HabitEditFragment.Companion.COLLECTED_HABIT
import com.example.crazy_habits.viewmodels.ColorViewModel

class ColorHabitFragment : Fragment() {
    private var _binding: FragmentColorHabitBinding? = null
    private val binding get() = _binding!!
    private val colorViewModel: ColorViewModel by viewModels{ColorViewModel.Factory}
    private var partHabitNumberOfBox: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!requireArguments().isEmpty) {
            arguments?.let {
                val habit = it.getParcelable<Habit>(COLLECTED_HABIT)!!
                partHabitNumberOfBox = if (colorViewModel.isNew(habit.id)) {
                    mutableListOf(habit.id, habit.colorHabit.toString(), "0")
                } else {
                    mutableListOf(
                        habit.id,
                        colorViewModel.getColorOfPressedBox().toString(),
                        colorViewModel.getNumberOfPressedBox().toString()
                    )
                }
                colorViewModel.saveData(partHabitNumberOfBox)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColorHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val colorBoxesList: MutableList<View> = mutableListOf()
        val screenWidth = requireContext().resources.displayMetrics.widthPixels

        for (i in 0..15) colorBoxesList.add(createColorBox(context, binding.colorLinear))

        setColorForBoxAndSaveColorAndNumberOfSelected(colorBoxesList)


 /*
 * observe изменение параметра viewModel, чтобы раскрасить центральный бокс, задать rgb код и
 * прокручивание ряда цветных боксов на центр экрана
 * */
        colorViewModel.listPartHabit.observe(viewLifecycleOwner) {
            binding.chosenColor.background =
                ShapeColorBox(5, colorViewModel.getColorOfPressedBox())
            binding.colorCode.text = convertToRGBcode(colorViewModel.getColorOfPressedBox())
            binding.hsv.smoothScrollTo(
                getCenterColor(colorBoxesList[colorViewModel.getNumberOfPressedBox()]).x - screenWidth/2,
                0
            )
        }

//передаем выбранный цвет обратно в listHabitsFragment
        binding.setColorButton.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                COLOR_HABIT,
                Bundle().apply { putInt(COLOR_HABIT, colorViewModel.getColorOfPressedBox()) })
            findNavController().popBackStack()
        }
    }

    private fun setColorForBoxAndSaveColorAndNumberOfSelected(colorBoxesList: MutableList<View>) {
        var cbCenter: Point
        val colorOfBox: MutableList<Int> = mutableListOf()
        val backgroundView: GradientDrawable = linearGradientDrawable()

        binding.colorLinear.doOnLayout {
            binding.colorLinear.background = linearGradientDrawable()
            val bitmap = convertToBitmap(backgroundView, it.measuredWidth, it.measuredHeight)
            for (i in 0..15) {
                cbCenter = getCenterColor(colorBoxesList[i])
                backgroundColorBox(bitmap, colorBoxesList[i], cbCenter, colorOfBox)
                colorBoxesList[i].setOnClickListener {
                    partHabitNumberOfBox[1] = colorOfBox[i].toString()
                    partHabitNumberOfBox[2] = i.toString()
                    colorViewModel.saveData(partHabitNumberOfBox)
                }
            }
        }
    }

    private fun createColorBox(ct: Context?, linear1: LinearLayout): View {
        val mutList: MutableList<View> = mutableListOf()
        val colorBox = View(ct)
        colorBox.layoutParams = LinearLayout.LayoutParams(
            widthOfBox.dpToPx,
            heightOfBox.dpToPx,
        ).apply {
            gravity = Gravity.CENTER
        }
        val margins = colorBox.layoutParams as ViewGroup.MarginLayoutParams
        margins.topMargin = 100.dpToPx
        margins.marginEnd = marginBox.dpToPx
        colorBox.layoutParams = margins
        linear1.addView(colorBox)
        mutList.add(colorBox)
        return colorBox
    }

    private fun backgroundColorBox(
        bitmap: Bitmap?,
        colorBox: View,
        cbCenter: Point,
        colorOfBox: MutableList<Int>
    ) {
        val pixelColor = bitmap!!.getPixel(cbCenter.x, cbCenter.y)
        colorOfBox.add(pixelColor)
        colorBox.background = ShapeColorBox(3, pixelColor)
    }

    private fun convertToRGBcode(pixelColor: Int): String {
//        val A: Int = pixelColor shr 24 and 0xff // or color >>> 24
        val R: Int = pixelColor shr 16 and 0xff
        val G: Int = pixelColor shr 8 and 0xff
        val B: Int = pixelColor and 0xff
        return "($R, $G, $B)"
    }


    private fun convertToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap? {
        if (drawable is BitmapDrawable) return drawable.bitmap
        val bitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, widthPixels, heightPixels)
        drawable.draw(canvas)
        return bitmap
    }

    // method to generate linear gradient drawable
    private fun linearGradientDrawable(): GradientDrawable {
        return GradientDrawable().apply {
            colors = intArrayOf(
                Color.parseColor("#FFFF0000"),
                Color.parseColor("#ff9500"), //
                Color.parseColor("#ffd900"),
                Color.parseColor("#4dff00"),
                Color.parseColor("#00ff8c"),
                Color.parseColor("#00ffaa"),
                Color.parseColor("#006aff"), //
                Color.parseColor("#a200ff"), //
                Color.parseColor("#ff00ea"),
                Color.parseColor("#ff0011")
            )
            gradientType = GradientDrawable.LINEAR_GRADIENT
            shape = GradientDrawable.RECTANGLE
            orientation = GradientDrawable.Orientation.LEFT_RIGHT

            // border around drawable
//            setStroke(5,Color.parseColor("#4B5320"))
        }
    }

    private fun getCenterColor(colorBox: View): Point {
        val cbLoc: Point = colorBox.getRelativeLocation()
        return Point(colorBox.width / 2 + cbLoc.x, colorBox.height / 2 + cbLoc.y)
    }

    private fun View.getRelativeLocation(): Point {
        return Point(this.x.toInt(), this.y.toInt())
    }

    /**
     * Converts Pixel to DP.
     */
    val Int.pxToDp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()

    /**
     * Converts DP to Pixel.
     */
    val Int.dpToPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    companion object {
        const val COLOR_HABIT = "colorHabit"
        private const val widthOfBox = 100
        private const val heightOfBox = 100
        private const val marginBox = 25
    }
}