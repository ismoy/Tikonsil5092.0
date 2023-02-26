package com.tikonsil.tikonsil509.presentation.mercadoPago

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tikonsil.tikonsil509.data.local.db.UsersDatabase
import com.tikonsil.tikonsil509.data.local.entity.MercadoPagoCredencials
import com.tikonsil.tikonsil509.data.local.entity.Product
import com.tikonsil.tikonsil509.domain.model.mercadoPago.MercadoPagoCardTokenBody
import com.tikonsil.tikonsil509.domain.model.mercadoPago.Payment
import com.tikonsil.tikonsil509.domain.repository.mercadoPago.MercadoPagoRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MercadoPagoViewModel(application: Application):AndroidViewModel(application) {

   private val repository :MercadoPagoRepository

    private val _responseMercadoPago:MutableLiveData<Response<JsonObject>> by lazy { MutableLiveData() }
    val responseMercadoPago:LiveData<Response<JsonObject>> = _responseMercadoPago

    private val _getInstallment:MutableLiveData<Response<JsonArray>> by lazy { MutableLiveData() }
    val getInstallment:LiveData<Response<JsonArray>> = _getInstallment

    private val _getAllData:MutableLiveData<List<MercadoPagoCredencials>> by lazy { MutableLiveData() }
    val getAllData:LiveData<List<MercadoPagoCredencials>> = _getAllData

    private val _getListProduct:MutableLiveData<List<Product>> by  lazy { MutableLiveData() }
    val getListProduct:LiveData<List<Product>> = _getListProduct

    private val _createPaymentMercadoPago:MutableLiveData<Response<JsonObject>> by lazy { MutableLiveData() }
    val createPaymentMercadoPago:LiveData<Response<JsonObject>> = _createPaymentMercadoPago

   private val _isLoading:MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isLoading : LiveData<Boolean> =_isLoading

    init {
       val mercadoPagoCredencialsDao =UsersDatabase.getDatabase(application).mercadoPagoCredentialsDao()
        val productDao = UsersDatabase.getDatabase(application).productDao()
        repository = MercadoPagoRepository(mercadoPagoCredencialsDao,productDao)
    }

    fun createCardToken(mercadoPagoCardTokenBody: MercadoPagoCardTokenBody){
        viewModelScope.launch {
            val result = repository.createCardToken(mercadoPagoCardTokenBody)
            _responseMercadoPago.value = result
        }
    }

    fun getInstallment(bin:String,amount:String){
        viewModelScope.launch {
            val result = repository.getInstallment(bin, amount)
            _getInstallment.value = result
        }
    }

    fun insertCredential(mercadoPagoCredencials: MercadoPagoCredencials){
        viewModelScope.launch {
            repository.insertCredential(mercadoPagoCredencials)
        }
    }

    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun getAllData(){
        viewModelScope.launch {
            val result = repository.getAllData()
            _getAllData.value = result
        }
    }

    fun insertProduct(product: Product){
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }

    fun deleteProduct(){
        viewModelScope.launch {
            repository.deleteProduct()
        }
    }

    fun getProduct(){
        viewModelScope.launch {
            val result = repository.getProduct()
            _getListProduct.value = result
        }
    }

    fun createPayment(payment: Payment){
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.createPayment(payment)
            _createPaymentMercadoPago.value = result
        }
    }
}