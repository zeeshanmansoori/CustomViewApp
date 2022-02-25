package com.google.customviewapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.customviewapp.databinding.EnterBtmSheetBinding

class EnterTextBtmSheet(val execute: (String) -> Unit) : BottomSheetDialogFragment() {
    private var binding: EnterBtmSheetBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = EnterBtmSheetBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {

            submit.setOnClickListener {
                execute.invoke(editText.text.toString())
                dismiss()
            }
        }
    }
}