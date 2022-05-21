package com.tikonsil.tikonsil509.presentation.mercadopago

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tikonsil.tikonsil509.domain.model.MercadoPagoCardTokenBody
import com.tikonsil.tikonsil509.domain.repository.mercadopago.MercadoPagoRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 19/05/2022. */
class MercadoPagoViewModel(private val repository: MercadoPagoRepository):ViewModel() {
    val myResponseMercadopago: MutableLiveData<Response<JsonObject>> by lazy { MutableLiveData() }

    fun createToken(mercadoPagoCardTokenBody: MercadoPagoCardTokenBody){
        viewModelScope.launch {
            val response = repository.createToken(mercadoPagoCardTokenBody)
            myResponseMercadopago.value = response
        }
    }
}