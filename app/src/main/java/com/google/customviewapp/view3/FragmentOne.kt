package com.google.customviewapp.view3

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.customviewapp.R
import com.google.customviewapp.databinding.FragmentOneBinding

class FragmentOne : Fragment(R.layout.fragment_one) {

    private lateinit var binding: FragmentOneBinding
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOneBinding.bind(view)
        val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        var width = requireActivity().windowManager.currentWindowMetrics.bounds.width()
        width = requireActivity().windowManager.defaultDisplay.width
        Log.d("zeeshan", "onViewCreated: displaywidth $width")
        Log.d("zeeshan", "onViewCreated: dropdownwidth ${binding.autoComplete.dropDownWidth}")
        val offset = width - binding.autoComplete.dropDownWidth
        binding.autoComplete.dropDownHorizontalOffset = offset
        binding.autoComplete.setAdapter(adapter)

    }
}