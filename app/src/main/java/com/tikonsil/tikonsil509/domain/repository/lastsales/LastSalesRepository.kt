package com.tikonsil.tikonsil509.domain.repository.lastsales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tikonsil.tikonsil509.data.remote.provider.LastSalesProvider
import com.tikonsil.tikonsil509.domain.model.LastSales

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class LastSalesRepository {
 private val lastsaleprovider by lazy { LastSalesProvider() }
    var isSnapShotExist =MutableLiveData<Boolean>()
    suspend fun getLastSales(idUser:String): LiveData<MutableList<LastSales>> {
        val mutableLiveData = MutableLiveData<MutableList<LastSales>>()
        lastsaleprovider.getLastSales(idUser)?.addValueEventListener(object : ValueEventListener {
            val listlastsalesdata = mutableListOf<LastSales>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val subtotal = ds.child("subtotal").value.toString()
                        val date = ds.child("date").value.toString()
                        val codecountry = ds.child("codecountry").value.toString()
                        val typerecharge = ds.child("typerecharge").value.toString()
                        val salesPrice = ds.child("salesPrice").value.toString()
                        val listlastsales = LastSales(subtotal,date,typerecharge, codecountry,salesPrice)
                        listlastsalesdata.add(listlastsales)
                    }
                    mutableLiveData.value =listlastsalesdata
                    isSnapShotExist.value =true
                }else{
                    isSnapShotExist.value =false
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }

}