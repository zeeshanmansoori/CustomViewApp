package com.google.customviewapp.view3

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.customviewapp.R
import com.google.customviewapp.databinding.FragmentOneBinding
import com.google.customviewapp.databinding.FragmentTwoBinding

class FragmentTwo : Fragment(R.layout.fragment_two) {
    private lateinit var binding: FragmentTwoBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTwoBinding.bind(view)
    }
}