package com.tikonsil.tikonsil509.domain.repository.sendrecharge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tikonsil.tikonsil509.data.remote.provider.IdProductProvider
import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.Headers
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.CostInnoverit
import com.tikonsil.tikonsil509.domain.model.Sales
import com.tikonsil.tikonsil509.domain.model.SendRechargeProduct
import com.tikonsil.tikonsil509.domain.model.SendRechargeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class SendRechargeRepository {

    private val getIdProduct by lazy { IdProductProvider() }
    var noExistSnapshot =MutableLiveData<Boolean>()

    suspend fun sales(uidUserCodeProduct:String,sales: Sales): Response<Sales> {
        val _tikonsilApi= RetrofitInstance(FirebaseApi.getFSApis().base_url_firebase_instance).tikonsilApi
        return _tikonsilApi.sales(uidUserCodeProduct,sales)
    }

    suspend fun salesWithErrorInnoverit(sales: Sales): Response<Sales> {
        val _tikonsilApi= RetrofitInstance(FirebaseApi.getFSApis().base_url_firebase_instance).tikonsilApi
        return _tikonsilApi.salesWithErrorInnoverit(FirebaseApi.getFSApis().end_point_save_sales_error_innoverit,sales)
    }

    suspend fun sendRechargeViaInnoVit(sendRechargeProduct: SendRechargeProduct): Result<Call<SendRechargeResponse>> {
        return runCatching {
            val _tikonsilApi = RetrofitInstance(FirebaseApi.getFSApis().base_url_tikonsil).tikonsilApi
            val response = withContext(Dispatchers.IO){
                _tikonsilApi.sendProduct(Headers.getHeaderTikonsil509(),FirebaseApi.getFSApis().end_point_send_product,
                sendRechargeProduct)
            }
            response
        }
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