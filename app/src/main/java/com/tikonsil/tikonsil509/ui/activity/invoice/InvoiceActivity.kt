package com.tikonsil.tikonsil509.ui.activity.invoice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tikonsil.tikonsil509.databinding.ActivityIvoiceBinding
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity

class InvoiceActivity :GenerateInvoice<ActivityIvoiceBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.generalpfd.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        showDataIntent()


    }
    override fun getActivityBinding()= ActivityIvoiceBinding.inflate(layoutInflater)

}