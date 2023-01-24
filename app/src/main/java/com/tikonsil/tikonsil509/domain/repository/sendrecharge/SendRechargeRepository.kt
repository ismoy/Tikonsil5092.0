package com.tikonsil.tikonsil509.domain.repository.sendrecharge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tikonsil.tikonsil509.data.remote.api.RetrofitInstance
import com.tikonsil.tikonsil509.data.remote.api.RetrofitInstanceApiRechargeTikonsil509
import com.tikonsil.tikonsil509.data.remote.provider.IdProductProvider
import com.tikonsil.tikonsil509.data.remote.provider.PriceCostProvider
import com.tikonsil.tikonsil509.domain.model.CostInnoverit
import com.tikonsil.tikonsil509.domain.model.Sales
import com.tikonsil.tikonsil509.domain.model.SendRecharge
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class SendRechargeRepository {

    private val getIdProduct by lazy { IdProductProvider() }
    var noExistSnapshot =MutableLiveData<Boolean>()
    var headersKey =HashMap<String,String>()

    suspend fun Sales(sales: Sales): Response<Sales> {
        return RetrofitInstance.tikonsilApi.Sales(sales)
    }

    init {
        GlobalScope.launch {
            val  apikey = RetrofitInstance.tikonsilApi.getKeyAuthorization()
             if (apikey.isSuccessful){
                 val headers = HashMap<String,String>()
                 headers["Authorization"] = apikey.body()!!.key
                 headersKey = headers
             }
        }
    }

    suspend fun sendRechargeViaInnoVit(idProduct:String,destination:String): Call<SendRecharge> {
        return RetrofitInstanceApiRechargeTikonsil509.tikonsilApi.sendProduct(idProduct,destination,headersKey)
    }

    fun getIdProductSelected(countryCode:String):LiveData<List<CostInnoverit>>{
        val mutableLiveData =MutableLiveData<List<CostInnoverit>>()
        getIdProduct.getIdProductSelected()?.orderByChild("country")?.equalTo(countryCode)
            ?.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listPriceCost = mutableListOf<CostInnoverit>()
                    if (snapshot.exists()){
                        for (ds in snapshot.children){
                            val country=ds.child("country").value.toString()
                            val idProduct =ds.child("idProduct").value.toString()
                            val nameMoneyCountryReceiver = ds.child("nameMoneyCountryReceiver").value.toString()
                            val nameMoneyCountrySale = ds.child("nameMoneyCountrySale").value.toString()
                            val operatorName =ds.child("operatorName").value.toString()
                            val priceReceiver = ds.child("priceReceiver").value.toString()
                            val priceSale =ds.child("priceSales").value.toString()
                            val formatPrice = ds.child("formatPrice").value.toString()
                            val listas =CostInnoverit(priceReceiver,operatorName,priceSale,nameMoneyCountryReceiver,nameMoneyCountrySale,idProduct, country,formatPrice)
                            listPriceCost.add(listas)
                        }
                        mutableLiveData.value =listPriceCost
                    }else{
                        noExistSnapshot.value =true
                        Log.e("noexiste","no existe db")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("noexiste","$error")

                }

            })
        return mutableLiveData
    }
}