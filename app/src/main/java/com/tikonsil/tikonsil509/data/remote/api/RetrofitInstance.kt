package com.tikonsil.tikonsil509.data.remote.api

import com.tikonsil.tikonsil509.utils.Constant.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
object RetrofitInstance {
 private val retrofit by lazy {
  Retrofit.Builder()
   .baseUrl(BASE_URL)
   .addConverterFactory(GsonConverterFactory.create())
   .build()
 }
 val tikonsilApi:TikonsilApi by lazy {
  retrofit.create(TikonsilApi::class.java)
 }
}