package com.example.crazy_habits

import android.R.attr.*
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)


        fun View.getLocationOnScreen(): Point
        {
            val location = IntArray(2)
            this.getLocationOnScreen(location)
            return Point(location[0],location[1])
        }


        val setColorButton : Button = findViewById(R.id.setColorButton)
        setColorButton.setOnClickListener{
                val intent = Intent()
                setResult(RESULT_OK, intent)
            val cv1 : View = findViewById(R.id.cv1)
            val cv2 : View = findViewById(R.id.cv2)
            val cv3 : View = findViewById(R.id.cv3)

//            Log.d ( "third", pxToDp(280).toString())
            val cv1Loc = cv1.getLocationOnScreen()
            val cv1X = cv1Loc.x
            val cv1Y = cv1Loc.y
            val cv1Center = Point( cv1.width/2+cv1Loc.x , cv1.height/2+cv1Loc.y)

            val cv7 : View = findViewById(R.id.cv7)
            val cv7Loc = cv7.getLocationOnScreen()
            val cv7Center = Point( cv7.width/2+cv7Loc.x , cv7.height/2+cv7Loc.y)


            Log.d ( "third", cv1X.toString())
            Log.d ( "third", cv1Center.toString())
            Log.d ( "third", cv1Center.x.pxToDp.toString())
            Log.d ( "third", cv1Center.y.pxToDp.toString())

            val ln1 : View = findViewById(R.id.linear1)
            val  bitmap = Bitmap.createBitmap(ln1.width, ln1.height, Bitmap.Config.ARGB_8888)
            val pixelColor1 = bitmap.getPixel(cv1Center.x , cv1Center.y)

            cv1.setBackgroundColor(pixelColor1)


            val pixelColor7 = bitmap.getPixel(cv7Center.x , cv7Center.y)
            cv7.setBackgroundColor(pixelColor7)







//            finish()

        }







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
}