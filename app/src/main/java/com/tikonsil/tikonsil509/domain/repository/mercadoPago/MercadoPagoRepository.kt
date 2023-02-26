package com.tikonsil.tikonsil509.domain.repository.mercadoPago

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tikonsil.tikonsil509.data.local.dao.MercadoPagoCredencialsDao
import com.tikonsil.tikonsil509.data.local.dao.ProductDao
import com.tikonsil.tikonsil509.data.local.entity.MercadoPagoCredencials
import com.tikonsil.tikonsil509.data.local.entity.Product
import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.Headers
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.mercadoPago.MercadoPagoCardTokenBody
import com.tikonsil.tikonsil509.domain.model.mercadoPago.Payment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MercadoPagoRepository(private val mercadoPagoCredencialsDao: MercadoPagoCredencialsDao,
                            private val productDao: ProductDao) {

    suspend fun createCardToken(mercadoPagoCardTokenBody: MercadoPagoCardTokenBody): Response<JsonObject> = withContext(Dispatchers.IO) {
        val _tikonsilApi =
            RetrofitInstance(FirebaseApi.getFSApis().base_url_mercado_pago_create_token).tikonsilApi
        return@withContext _tikonsilApi.createCardToken(
            FirebaseApi.getFSApis().end_point_mercado_pago_create_token+FirebaseApi.getFSApis().public_key_mercado_pago ,
            mercadoPagoCardTokenBody
        )
    }

    suspend fun getInstallment(bin:String,amount:String):Response<JsonArray> = withContext(Dispatchers.IO){
        val _tikonsilApi =
            RetrofitInstance(FirebaseApi.getFSApis().base_url_mercado_pago_create_token).tikonsilApi
        return@withContext _tikonsilApi.getInstallments(FirebaseApi.getFSApis().end_point_installments+FirebaseApi.getFSApis().access_token_mercado_pago,bin, amount)
    }

    suspend fun insertCredential(mercadoPagoCredencials: MercadoPagoCredencials) = withContext(Dispatchers.IO){
        return@withContext mercadoPagoCredencialsDao.insetCredential(mercadoPagoCredencials)
    }

    suspend fun deleteAll() = withContext(Dispatchers.IO){
        return@withContext mercadoPagoCredencialsDao.deleteAll()
    }

    suspend fun getAllData():List<MercadoPagoCredencials> = withContext(Dispatchers.IO){
        return@withContext mercadoPagoCredencialsDao.getAllData()
    }

    suspend fun insertProduct(product: Product) = withContext(Dispatchers.IO){
        return@withContext productDao.insertProduct(product)
    }

    suspend fun deleteProduct() = withContext(Dispatchers.IO){
        return@withContext productDao.deleteAll()
    }

    suspend fun getProduct():List<Product> = withContext(Dispatchers.IO){
        return@withContext productDao.getAllData()
    }

    suspend fun createPayment(payment: Payment):Response<JsonObject> = withContext(Dispatchers.IO){
        val _tikonsil509 =
            RetrofitInstance(FirebaseApi.getFSApis().base_url_tikonsil).tikonsilApi
        return@withContext _tikonsil509.createPayWithMercadoPago(Headers.getHeaderTikonsil509(),FirebaseApi.getFSApis().
        end_point_mercado_pago_payment,payment)
    }
}