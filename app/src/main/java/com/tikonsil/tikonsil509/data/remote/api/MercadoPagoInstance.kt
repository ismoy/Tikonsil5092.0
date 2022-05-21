package com.tikonsil.tikonsil509.data.remote.api

import com.tikonsil.tikonsil509.utils.Constant.Companion.BASE_URL
import com.tikonsil.tikonsil509.utils.Constant.Companion.BASE_URL_MERCADOPAGO
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
object MercadoPagoInstance {
 private val retrofit by lazy {
  Retrofit.Builder()
   .baseUrl(BASE_URL_MERCADOPAGO)
   .addConverterFactory(GsonConverterFactory.create())
   .build()
 }
 val mercadopagoapi:TikonsilApi by lazy {
  retrofit.create(TikonsilApi::class.java)
 }
}