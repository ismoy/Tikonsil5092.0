package com.tikonsil.tikonsil509.ui.fragment.sendrecharge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.databinding.FragmentSendRechargeBinding
import com.tikonsil.tikonsil509.presentation.countryprices.CountryPricesViewModel

class SendRechargeFragment :ValidateFormSales<FragmentSendRechargeBinding,CountryPricesViewModel>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validateitemrealtime()
        recargar?.setOnClickListener {
            validateOnclickButton()
        }
        getDataUser()
        getPriceCountry()
    }


    override fun getViewModel()=CountryPricesViewModel::class.java
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?)= FragmentSendRechargeBinding.inflate(inflater,container,false)

}