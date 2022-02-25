package com.google.customviewapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.customviewapp.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private val binding: ActivityMain2Binding by lazy {
        ActivityMain2Binding.inflate(layoutInflater)
    }

    var scaleFactor = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {


            increase.setOnClickListener {
                scaleFactor += .25f
                name.scaleX = scaleFactor
                name.scaleY = scaleFactor
                nameWidth.text = name.toString()

                decrease.isEnabled = !decrease.isEnabled

            }

            decrease.setOnClickListener {
                scaleFactor -= .25f
                name.scaleX = scaleFactor
                name.scaleY = scaleFactor
                nameWidth.text = name.toString()
            }


        }
    }
}