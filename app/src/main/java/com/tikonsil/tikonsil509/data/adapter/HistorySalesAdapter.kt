package com.tikonsil.tikonsil509.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.databinding.HistorySaleItemBinding
import com.tikonsil.tikonsil509.domain.model.Sales

/** * Created by ISMOY BELIZAIRE on 02/05/2022. */
class HistorySalesAdapter(val context: Context): RecyclerView.Adapter<HistorySalesAdapter.MyViewHolder>() {
 private var saleList = mutableListOf<Sales>()
 fun setsaleListDataHistory(data:MutableList<Sales>) {
  saleList = data
 }
 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
 val view = HistorySaleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
  return MyViewHolder(view)
 }

 override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
  val saledata =saleList[position]
  holder.bidView(saledata,context)
 }

 override fun getItemCount(): Int {
  return saleList.size
 }
 class MyViewHolder(private val binding: HistorySaleItemBinding) :  RecyclerView.ViewHolder(binding.root) {
  fun bidView(saledata: Sales, context: Context) {
   binding.apply {
    firstnameinvoicesagente.text =saledata.firstname
    lastnameinvoicesagente.text =saledata.lastname
    emailinvoicesagente.text =saledata.email
    saledata.role.let {
     if (it==1){
      roleinvoicesagente.text =context.getString(R.string.agent)
     }else{
      roleinvoicesagente.text =context.getString(R.string.master)
     }
    }
    telefonoinvoiceagente.text =saledata.phone
    fechainvoiceagente.text =saledata.date
    tiporecargainvoiceagente.text =saledata.typerecharge
    paisinvoiceagente.text = saledata.country
    subtotalinvoiceagente.text =saledata.subtotal
    descriptioninvoiceagente.text =saledata.description
   }
  }

 }
}