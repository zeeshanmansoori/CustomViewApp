package com.google.customviewapp.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.customviewapp.R
import com.google.customviewapp.databinding.ActionVideoBinding

class DemoActivity : AppCompatActivity(R.layout.action_video) {

    private val binding: ActionVideoBinding by lazy {
        ActionVideoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }


}