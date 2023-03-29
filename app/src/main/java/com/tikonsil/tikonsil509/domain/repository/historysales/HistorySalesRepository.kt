package com.tikonsil.tikonsil509.domain.repository.historysales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tikonsil.tikonsil509.data.remote.provider.HistorySalesProvider
import com.tikonsil.tikonsil509.domain.model.Sales

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class HistorySalesRepository {
    private val historysalesprovider by lazy { HistorySalesProvider() }

    var isExistSnapShot=MutableLiveData<Boolean>()

    suspend fun getHistorySales(idUser:String): LiveData<MutableList<Sales>> {
        val mutableLiveDat = MutableLiveData<MutableList<Sales>>()
        historysalesprovider.getHistorySales(idUser)?.addValueEventListener(object : ValueEventListener {
            val listlastdata = mutableListOf<Sales>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val firstname = ds.child("firstname").value.toString()
                        val lastname =ds.child("lastname").value.toString()
                        val phone = ds.child("phone").value.toString()
                        val role =ds.child("role").value.toString()
                        val email =ds.child("email").value.toString()
                        val description = ds.child("description").value.toString()
                        val subtotal = ds.child("subtotal").value.toString()
                        val date = ds.child("date").value.toString()
                        val codecountry = ds.child("codecountry").value.toString()
                        val typerecharge = ds.child("typerecharge").value.toString()
                        val token = ds.child("token").value.toString()
                        val status = ds.child("status").value.toString()
                        val idProduct = ds.child("id_product").value.toString()
                        val salePrice = ds.child("salesPrice").value.toString()
                        val imageView = ds.child("image").value.toString()
                        val idUser = ds.child("idUser").value.toString()
                        val salesPriceSubtotal = ds.child("salesPriceSubtotal").value.toString()
                        val salesPriceFee = ds.child("salesPriceFee").value.toString()
                        val listsales = Sales(idUser,firstname,lastname, email,role.toInt(),typerecharge, phone, date,codecountry,codecountry, subtotal, description,token,status.toInt(),idProduct.toInt(),salePrice,imageView,salesPriceSubtotal, salesPriceFee)
                        listlastdata.add(listsales)
                    }
                    mutableLiveDat.value =listlastdata
                }else{
                    isExistSnapShot.value=true
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveDat
    }

}