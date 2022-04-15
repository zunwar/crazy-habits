package com.example.crazy_habits

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import com.example.crazy_habits.databinding.ActivityThirdBinding


class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var cbCenter: Point
        val colorOfBoxRGB : MutableList<String> = mutableListOf()
        val colorOfBox : MutableList<Int> = mutableListOf()
        val colorBoxesList: MutableList<View> = mutableListOf()
        var colorForIntent = 0
        val backgroundView: GradientDrawable = linearGradientDrawable()

        for (i in 1..16) {
            colorBoxesList.add(createColorBox(this, binding.colorLinear))
        }
        binding.colorLinear.doOnLayout {
            binding.colorLinear.background = linearGradientDrawable()
            val bitmap = convertToBitmap(backgroundView, it.measuredWidth, it.measuredHeight)

            for (i in 0..15) {
                cbCenter = getCenterColor(colorBoxesList[i])
                backgroundColorBox(bitmap, colorBoxesList[i], cbCenter , colorOfBoxRGB, colorOfBox)
                colorBoxesList[i].setOnClickListener {
                    binding.chosenColor.background = ShapeColorBox(5, colorOfBox[i])
                    binding.colorCode.text = colorOfBoxRGB[i]
                    colorForIntent = colorOfBox[i]
                }
            }
            binding.setColorButton.setOnClickListener {
                val intent = Intent()
                setResult(RESULT_OK, intent)
                intent.putExtra(COLOR_HABIT, colorForIntent)
                finish()
            }
        }
    }



    private fun createColorBox(ct : Context, linear1 : LinearLayout) : View {
        val mutlist : MutableList<View> = mutableListOf()
            val colorBox = View(ct)
            colorBox.layoutParams = LinearLayout.LayoutParams(
                100.dpToPx,
                100.dpToPx,
            ).apply {
                gravity = Gravity.CENTER
            }
            val margins = colorBox.layoutParams as ViewGroup.MarginLayoutParams
            margins.topMargin = 100.dpToPx
            margins.marginEnd = 25.dpToPx
            colorBox.layoutParams = margins
            linear1.addView(colorBox)
            mutlist.add(colorBox)
        return colorBox
    }

    private fun backgroundColorBox(
        bitmap: Bitmap?,
        colorBox: View,
        cbCenter: Point,
        colorOfBoxRGB: MutableList<String>,
        colorOfBoxInt: MutableList<Int>
    ) {
        val pixelColor = bitmap!!.getPixel(cbCenter.x, cbCenter.y)
//        val A: Int = pixelColor shr 24 and 0xff // or color >>> 24
        val R: Int = pixelColor shr 16 and 0xff
        val G: Int = pixelColor shr 8 and 0xff
        val B: Int = pixelColor and 0xff
        val colorOfBoxString = "($R, $G, $B)"
        colorOfBoxRGB.add(colorOfBoxString)
        colorOfBoxInt.add(pixelColor)
        colorBox.background = ShapeColorBox(3, pixelColor)
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

    private fun getCenterColor(colorBox : View): Point {
        val cbLoc: Point = colorBox.getLocationOnScreen()
        return Point(colorBox.width / 2 + cbLoc.x, colorBox.height / 2 + cbLoc.y)
    }

    private fun View.getLocationOnScreen(): Point {
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        return Point(location[0], location[1])
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
        private const val COLOR_HABIT = "colorHabit"
    }

}