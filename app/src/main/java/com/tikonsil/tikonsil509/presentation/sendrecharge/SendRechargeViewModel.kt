package com.tikonsil.tikonsil509.presentation.sendrecharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.CostInnoverit
import com.tikonsil.tikonsil509.domain.model.Sales
import com.tikonsil.tikonsil509.domain.model.SendRechargeProduct
import com.tikonsil.tikonsil509.domain.model.SendRechargeResponse
import com.tikonsil.tikonsil509.domain.repository.sendrecharge.SendRechargeRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class SendRechargeViewModel(private val repository: SendRechargeRepository) :ViewModel(){

    val myResponseSales: MutableLiveData<Response<Sales>> by lazy { MutableLiveData() }

    val responseGetAllPrice =MutableLiveData<List<CostInnoverit>>()

    var noExistSnapshot = MutableLiveData<Boolean>()

    val responseIdProduct:MutableLiveData<List<CostInnoverit>> by lazy { MutableLiveData() }

    private val _responseInnoVit:MutableLiveData<Result<Call<SendRechargeResponse>>> by lazy { MutableLiveData() }
    val responseInnoVit : LiveData<Result<Call<SendRechargeResponse>>> =_responseInnoVit

    init {
        noExistSnapshot =repository.noExistSnapshot
    }

    fun sales(uidUserCodeProduct:String,sales: Sales) {
        viewModelScope.launch {
            val response = repository.sales(uidUserCodeProduct, sales)
            myResponseSales.value = response
        }
    }

    fun salesWithErrorInnoverit(sales: Sales){
        viewModelScope.launch {
            repository.salesWithErrorInnoverit(sales)
        }

    }
    fun sendRechargeViaInnoVit(sendRechargeProduct: SendRechargeProduct){
        viewModelScope.launch {
            val response = repository.sendRechargeViaInnoVit(sendRechargeProduct)
            _responseInnoVit.value = response
        }
    }

    fun getIdProductSelected(countryCode:String){
        viewModelScope.launch {
            repository.getIdProductSelected(countryCode).observeForever {
                responseIdProduct.value =it
            }
        }
    }
}