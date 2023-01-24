package com.tikonsil.tikonsil509.ui.activity.invoice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.tikonsil.tikonsil509.databinding.ActivityIvoiceBinding
import com.tikonsil.tikonsil509.domain.model.Sales
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import java.io.FileOutputStream

class InvoiceActivity : AppCompatActivity() {
    private lateinit var binding:ActivityIvoiceBinding
    private var saleData:Sales?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
       saleData = intent.getParcelableExtra("saleData")

        setDataInView(saleData)
        binding.generalpfd.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }

    private fun setDataInView(saleData: Sales?) {

        if(saleData!=null){
            binding.apply {
                nameuser.text = saleData.firstname
                lastnameuser.text = saleData.lastname
                emailuser.text = saleData.email
                recargatypeuser.text = saleData.typerecharge
                paisuser.text = saleData.country
                subtotaluser.text = saleData.subtotal
                dateuser.text = saleData.date
                phoneuser.text = saleData.phone
                descripcionuser.text = saleData.description
            }
        }
    }

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        return super.getOnBackInvokedDispatcher()
    }

}