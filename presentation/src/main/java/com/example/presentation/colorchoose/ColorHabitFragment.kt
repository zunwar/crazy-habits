package com.example.presentation.colorchoose
//
//import android.content.Context
//import android.content.res.Resources
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Point
//import android.graphics.drawable.BitmapDrawable
//import android.graphics.drawable.Drawable
//import android.graphics.drawable.GradientDrawable
//import android.os.Bundle
//import android.util.Log
//import android.view.Gravity
import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import androidx.core.view.doOnLayout
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.findNavController
//import com.example.presentation.databinding.FragmentColorHabitBinding
//import com.example.presentation.edithabit.HabitEditFragment.Companion.COLLECTED_HABIT
//
class ColorHabitFragment : Fragment() {
//    private var _binding: FragmentColorHabitBinding? = null
//    private val binding get() = _binding!!
//    private val colorViewModel: ColorViewModel by viewModels {
//        ColorViewModel.provideFactory(
//            arguments?.getString("COLLECTED_HABIT")!!
//        )
//    }
//    private val colorBoxViewList: MutableList<View> = mutableListOf()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d(TAG, "ColorHabitFragment---onCreate ")
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentColorHabitBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        colorViewModel.saveIfNew()
//        createColorBoxes(context, binding.colorLinear)
//        createAndSave(colorBoxViewList)
//        setColorButton()
//        displaySelectedColorBox()
//        observeClose()
//    }
//
//    private fun observeClose() {
//        colorViewModel.closeColorFragment.observe(viewLifecycleOwner) {
//            findNavController().previousBackStackEntry?.savedStateHandle?.set(
//                COLOR_HABIT,
//                Bundle().apply { putInt(COLOR_HABIT, colorViewModel.colorBoxEntity.value!!.color) })
//            findNavController().popBackStack()
//        }
//    }
//
//    private fun setColorButton() {
//        //передаем выбранный цвет обратно в listHabitsFragment
//        binding.setColorButton.setOnClickListener {
//            colorViewModel.closeColorFragment()
//        }
//    }
//
//    private fun displaySelectedColorBox() {
//        val screenWidth = requireContext().resources.displayMetrics.widthPixels
//        colorViewModel.colorBoxEntity.observe(viewLifecycleOwner) { colorBoxEntity ->
//            binding.chosenColor.background =
//                ShapeColorBox(5, colorBoxEntity.color)
//            binding.colorCode.text = convertToRGBcode(colorBoxEntity.color)
//            colorViewModel.isDoneCreatingBoxes.observe(viewLifecycleOwner) {
//                binding.hsv.smoothScrollTo(
//                    getCenterColor(colorBoxViewList[colorBoxEntity.colorBoxNum.idBox - 1]).x - screenWidth / 2,
//                    0
//                )
//            }
//        }
//    }
//
//    private fun createAndSave(colorBoxesList: List<View>) {
//        var cbCenter: Point
//        val colorOfBox: MutableList<Int> = mutableListOf()
//        val backgroundView: GradientDrawable = linearGradientDrawable()
//
//        binding.colorLinear.doOnLayout {
//            binding.colorLinear.background = linearGradientDrawable()
//            val bitmap = convertToBitmap(backgroundView, it.measuredWidth, it.measuredHeight)
//            repeat(com.example.domain.entities.ColorBoxNum.Sixteen.idBox) { i ->
//                cbCenter = getCenterColor(colorBoxesList[i])
//                backgroundColorBox(bitmap, colorBoxesList[i], cbCenter, colorOfBox)
//                saveColorAndNumOfSelected(colorBoxesList[i], i, colorOfBox[i])
//            }
//            colorViewModel.isDoneCreatingBoxes(true)
//        }
//    }
//
//    private fun saveColorAndNumOfSelected(view: View, num: Int, color: Int) {
//        view.setOnClickListener {
//            colorViewModel.saveSelectedColorAndNum(color, num)
//        }
//    }
//
//    private fun createColorBoxes(ct: Context?, linear1: LinearLayout) {
//        repeat(com.example.domain.entities.ColorBoxNum.Sixteen.idBox) {
//            val colorBoxView = View(ct)
//            colorBoxView.layoutParams = LinearLayout.LayoutParams(
//                widthOfBox.dpToPx,
//                heightOfBox.dpToPx,
//            ).apply {
//                gravity = Gravity.CENTER
//            }
//            val margins = colorBoxView.layoutParams as ViewGroup.MarginLayoutParams
//            margins.topMargin = 100.dpToPx
//            margins.marginEnd = marginBox.dpToPx
//            colorBoxView.layoutParams = margins
//            linear1.addView(colorBoxView)
//            colorBoxViewList.add(colorBoxView)
//        }
//    }
//
//    private fun backgroundColorBox(
//        bitmap: Bitmap?,
//        colorBoxView: View,
//        cbCenter: Point,
//        colorOfBox: MutableList<Int>
//    ) {
//        val pixelColor = bitmap!!.getPixel(cbCenter.x, cbCenter.y)
//        colorOfBox.add(pixelColor)
//        colorBoxView.background = ShapeColorBox(3, pixelColor)
//    }
//
//    private fun convertToRGBcode(pixelColor: Int): String {
////        val A: Int = pixelColor shr 24 and 0xff // or color >>> 24
//        val R: Int = pixelColor shr 16 and 0xff
//        val G: Int = pixelColor shr 8 and 0xff
//        val B: Int = pixelColor and 0xff
//        return "($R, $G, $B)"
//    }
//
//
//    private fun convertToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap? {
//        if (drawable is BitmapDrawable) return drawable.bitmap
//        val bitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        drawable.setBounds(0, 0, widthPixels, heightPixels)
//        drawable.draw(canvas)
//        return bitmap
//    }
//
//    // method to generate linear gradient drawable
//    private fun linearGradientDrawable(): GradientDrawable {
//        return GradientDrawable().apply {
//            colors = intArrayOf(
//                Color.parseColor("#FFFF0000"),
//                Color.parseColor("#ff9500"), //
//                Color.parseColor("#ffd900"),
//                Color.parseColor("#4dff00"),
//                Color.parseColor("#00ff8c"),
//                Color.parseColor("#00ffaa"),
//                Color.parseColor("#006aff"), //
//                Color.parseColor("#a200ff"), //
//                Color.parseColor("#ff00ea"),
//                Color.parseColor("#ff0011")
//            )
//            gradientType = GradientDrawable.LINEAR_GRADIENT
//            shape = GradientDrawable.RECTANGLE
//            orientation = GradientDrawable.Orientation.LEFT_RIGHT
//
//            // border around drawable
////            setStroke(5,Color.parseColor("#4B5320"))
//        }
//    }
//
//    private fun getCenterColor(colorBoxView: View): Point {
//        val cbLoc: Point = colorBoxView.getRelativeLocation()
//        return Point(colorBoxView.width / 2 + cbLoc.x, colorBoxView.height / 2 + cbLoc.y)
//    }
//
//    private fun View.getRelativeLocation(): Point {
//        return Point(this.x.toInt(), this.y.toInt())
//    }
//
//    /**
//     * Converts Pixel to DP.
//     */
//    val Int.pxToDp: Int
//        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
//
//    /**
//     * Converts DP to Pixel.
//     */
//    val Int.dpToPx: Int
//        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
//
    companion object {
        const val COLOR_HABIT = "colorHabit"
//        private const val widthOfBox = 100
//        private const val heightOfBox = 100
//        private const val marginBox = 25
    }
}