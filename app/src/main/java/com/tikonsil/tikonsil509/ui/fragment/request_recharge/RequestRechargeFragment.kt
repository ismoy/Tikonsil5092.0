package com.tikonsil.tikonsil509.ui.fragment.request_recharge

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.databinding.FragmentRequestRechargeBinding
import com.tikonsil.tikonsil509.presentation.requestrecharge.RequestRechargeViewModel


class RequestRechargeFragment :RequestRechargeValidate<FragmentRequestRechargeBinding,RequestRechargeViewModel>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        selectemethodpayment()
        binding.clickToCopy.setOnClickListener {
            copyNumberAccount()
        }
        binding.clickToCopyBanreservas.setOnClickListener {
            copyNumberAccountReserva()
        }
        binding.clickToCopyZelle.setOnClickListener {
            copyNumberAccountZelle()
        }
        binding.clickToCopyPls.setOnClickListener {
            copyNumberAccountPL()
        }
        binding.clickToCopyPlsemail.setOnClickListener {
            copyEmailAccountPL()
        }

        cardmercadopago?.setOnClickListener {
            navController.navigate(R.id.action_requestRechargeFragment_to_mercadoPagoFragment)
        }
    }

    override fun getViewModel()=RequestRechargeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater, container: ViewGroup?)= FragmentRequestRechargeBinding.inflate(inflater,container,false)


}