package com.tikonsil.tikonsil509.ui.fragment.payNatMoncsah

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.databinding.FragmentOtherPaymentBinding

class OtherPaymentFragment : Fragment() {

    private lateinit var binding: FragmentOtherPaymentBinding
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtherPaymentBinding.inflate(inflater,container,false)
        return binding.root
    }
}