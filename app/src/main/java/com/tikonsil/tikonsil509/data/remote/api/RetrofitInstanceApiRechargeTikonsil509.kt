package com.tikonsil.tikonsil509.data.remote.api

import com.tikonsil.tikonsil509.utils.constants.Constant.Companion.BASE_URL_TIKONSIL509
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** * Created by ISMOY BELIZAIRE on 01/01/2023. */
object RetrofitInstanceApiRechargeTikonsil509 {

 private val retrofit by lazy {
  Retrofit.Builder()
   .baseUrl(BASE_URL_TIKONSIL509)
   .addConverterFactory(GsonConverterFactory.create())
   .build()
 }
 val tikonsilApi:TikonsilApi by lazy {
  retrofit.create(TikonsilApi::class.java)
 }
}