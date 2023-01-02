package com.tikonsil.tikonsil509.domain.repository.sendrecharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tikonsil.tikonsil509.data.remote.api.RetrofitInstance
import com.tikonsil.tikonsil509.data.remote.provider.IdProductProvider
import com.tikonsil.tikonsil509.data.remote.provider.PriceCostProvider
import com.tikonsil.tikonsil509.domain.model.CostInnoverit
import com.tikonsil.tikonsil509.domain.model.Sales
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class SendRechargeRepository {

    private val getPriceCost by lazy { PriceCostProvider() }

    private val getIdProduct by lazy { IdProductProvider() }
    var noExistSnapshot =MutableLiveData<Boolean>()
    suspend fun Sales(sales: Sales): Response<Sales> {
        return RetrofitInstance.tikonsilApi.Sales(sales)
    }

    fun getAllPrice(country:String): LiveData<List<CostInnoverit>> {

        val mutableLiveData = MutableLiveData<List<CostInnoverit>>()
        getPriceCost.getPriceCost().orderByChild("country").equalTo(country).addValueEventListener(object : ValueEventListener {
            val listPriceCost = mutableListOf<CostInnoverit>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        val country=ds.child("country").value.toString()
                        val idProduct =ds.child("id_product").value.toString()
                        val operatorName =ds.child("operatorName").value.toString()
                        val priceSale =ds.child("priceSale").value.toString()
                        val listas =CostInnoverit(country,operatorName,priceSale,idProduct)
                        listPriceCost.add(listas)
                    }
                    mutableLiveData.value =listPriceCost
                }else{
                   noExistSnapshot.value =true
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return mutableLiveData
    }


    fun getIdProductSelected(priceSales:String):MutableLiveData<String>{
        val mutableLiveData =MutableLiveData<String>()
        getIdProduct.getIdProductSelected()?.orderByChild("priceSales")?.equalTo(priceSales)
            ?.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (ds in snapshot.children){
                            val idProduct:String =ds.child("idProduct").value.toString()
                            mutableLiveData.value =idProduct
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        return mutableLiveData
    }
}