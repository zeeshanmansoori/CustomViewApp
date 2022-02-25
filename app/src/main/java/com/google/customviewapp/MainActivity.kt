package com.google.customviewapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.customviewapp.views.DrawerView
import com.google.customviewapp.views.EnterTextBtmSheet

/**
 * Make an app with a full-screen canvas view whiteboard and OCR the drawn text/ character.

• Should be able to draw text/character anywhere on the canvas view.

• When clicked on the drawn text/character, it should open a popup dialog with an edit text prefilled with the text that has been detected.

 * */
class MainActivity : AppCompatActivity(), DrawerView.DrawerListener {

    lateinit var canvas: DrawerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        canvas = findViewById<DrawerView>(R.id.my_canvas)
        canvas.setListener(this)
        findViewById<MaterialButton>(R.id.clear_btn).setOnClickListener {
            canvas.clearCanvas()

        }

        findViewById<MaterialButton>(R.id.apply_paint_btn).setOnClickListener {
            canvas.toggleFill()

        }


        val height = resources.displayMetrics.heightPixels
        val width = resources.displayMetrics.widthPixels
        Log.d("zeeshan", "onCreate: height $height  width $width")
    }

    override fun onclick() {
        val btmsheet = EnterTextBtmSheet(::drawText)
        btmsheet.show(supportFragmentManager, null)
    }

    private fun drawText(text: String) {
        canvas.drawText(text)
    }
}