package com.tikonsil.tikonsil509.ui.activity.invoice

import android.os.Bundle
import android.util.Log
import com.tikonsil.tikonsil509.databinding.ActivityIvoiceBinding

class InvoiceActivity :GenerateInvoice<ActivityIvoiceBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.generalpfd.setOnClickListener {
         generalpdf()
        }
        showDataIntent()


    }
    override fun getActivityBinding()= ActivityIvoiceBinding.inflate(layoutInflater)

}