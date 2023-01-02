package com.tikonsil.tikonsil509.data.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.databinding.ItemLastSaleBinding
import com.tikonsil.tikonsil509.domain.model.LastSales

/** * Created by ISMOY BELIZAIRE on 27/04/2022. */
class LastSaleAdapter(val context: Context): RecyclerView.Adapter<LastSaleAdapter.MyViewHolder>() {
 private var saleList = mutableListOf<LastSales>()
 fun setsaleListData(data:MutableList<LastSales>){
  saleList = data
 }


 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
 val view = ItemLastSaleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
  return MyViewHolder(view)
 }

 override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
  val saledata =saleList[position]
  holder.bidView(saledata,context)
 }

 override fun getItemCount(): Int {
  return if (saleList.size>0)
   saleList.size else{
    0
  }
 }
 inner class MyViewHolder(private val binding:ItemLastSaleBinding) :  RecyclerView.ViewHolder(binding.root) {
  @SuppressLint("CheckResult")
  fun bidView(saledata: LastSales, context: Context) {

      binding.apply {
        when {
         saledata.typerecharge =="MONCASH" && saledata.codecountry=="HT"->{
          Glide.with(context).load(R.drawable.haiti).into(imageViewPlaneta)
          saldoform.text = " HTG "+saledata.subtotal
          dateform.text =saledata.dates
          nameTypeRecharge.text= saledata.typerecharge
         }
         saledata.typerecharge=="TOPUP" && saledata.codecountry=="BR"->{
          Glide.with(context).load(R.drawable.brazil).into(imageViewPlaneta)
          saldoform.text = " BRL "+saledata.subtotal
          dateform.text =saledata.dates
          nameTypeRecharge.text= saledata.typerecharge
         }
         saledata.typerecharge=="TOPUP" && saledata.codecountry=="HT"->{
          Glide.with(context).load(R.drawable.haiti).into(imageViewPlaneta)
          saldoform.text = " HTG "+saledata.subtotal
          dateform.text =saledata.dates
          nameTypeRecharge.text= saledata.typerecharge
         }
         saledata.typerecharge=="TOPUP" && saledata.codecountry=="PA"->{
          Glide.with(context).load(R.drawable.panama).into(imageViewPlaneta)
          saldoform.text = " PAB "+saledata.subtotal
          dateform.text =saledata.dates
          nameTypeRecharge.text= saledata.typerecharge
         }
         saledata.typerecharge=="TOPUP" && saledata.codecountry=="DO"->{
          Glide.with(context).load(R.drawable.republicadominicana).into(imageViewPlaneta)
          saldoform.text = " DOP "+saledata.subtotal
          dateform.text =saledata.dates
          nameTypeRecharge.text= saledata.typerecharge
         }
         saledata.typerecharge=="TOPUP" && saledata.codecountry=="CU"->{
          Glide.with(context).load(R.drawable.cuba).into(imageViewPlaneta)
          saldoform.text =" CUP "+ saledata.subtotal
          dateform.text =saledata.dates
          nameTypeRecharge.text= saledata.typerecharge
         }
         saledata.typerecharge=="TOPUP" && saledata.codecountry=="MX"->{
          Glide.with(context).load(R.drawable.mexico).into(imageViewPlaneta)
          saldoform.text =" MXN "+ saledata.subtotal
          dateform.text =saledata.dates
          nameTypeRecharge.text= saledata.typerecharge

         }
         saledata.codecountry=="HT" && saledata.typerecharge==""->{
          saldoform.text =" $ "+ saledata.salePrice
          Glide.with(context).load(R.drawable.haiti).into(imageViewPlaneta)
          dateform.text =saledata.dates
          nameTypeRecharge.text= "TOPUP"
         }
         else->{
         saldoform.text =" $ "+ saledata.subtotal
         dateform.text =saledata.dates
         nameTypeRecharge.text= "TOPUP"
          Glide.with(context).load(R.drawable.planeta).into(imageViewPlaneta)
         }
        }
      }
  }

 }
}