package com.google.customviewapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.customviewapp.displayOverlay.ForegroundService
import com.google.customviewapp.views.DrawerView
import com.google.customviewapp.views.EnterTextBtmSheet
class MainActivity : AppCompatActivity(), DrawerView.DrawerListener {

    lateinit var canvas: DrawerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        canvas = findViewById(R.id.my_canvas)
        canvas.setListener(this)
        findViewById<MaterialButton>(R.id.clear_btn).setOnClickListener {
            canvas.clearCanvas()
        }

        findViewById<MaterialButton>(R.id.apply_paint_btn).setOnClickListener {
            canvas.toggleFill()

        }

        checkOverlayPermission()
        startService()
    }

    override fun onclick() {
        val btmsheet = EnterTextBtmSheet(::drawText)
        btmsheet.show(supportFragmentManager, null)
    }

    private fun drawText(text: String) {
        canvas.drawText(text)
    }

    private fun startService() {
        // check if the user has already granted
        // the Draw over other apps permission
        if (Settings.canDrawOverlays(this)) {
            // start the service based on the android version
            startForegroundService(Intent(this, ForegroundService::class.java))
        }
    }

    // method to ask user to grant the Overlay permission
    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            // send user to the device settings
            val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            startActivity(myIntent)
        }
    }
}