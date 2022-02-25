package com.google.customviewapp.view3

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.customviewapp.R
import com.google.customviewapp.databinding.FragmentOneBinding

class FragmentOne : Fragment(R.layout.fragment_one) {
    private lateinit var binding: FragmentOneBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOneBinding.bind(view)
    }
}