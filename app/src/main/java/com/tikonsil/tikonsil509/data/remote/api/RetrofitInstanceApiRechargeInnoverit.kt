package com.tikonsil.tikonsil509.data.remote.api

import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.tikonsil.tikonsil509.utils.constants.Constant.Companion.BASE_URL_INNOVIT
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
object RetrofitInstanceApiRechargeInnoverit {

 private val retrofit by lazy {
  Retrofit.Builder()
   .baseUrl(BASE_URL_INNOVIT)
   .addConverterFactory(GsonConverterFactory.create())
   .build()
 }
 val tikonsilApi:TikonsilApi by lazy {
  retrofit.create(TikonsilApi::class.java)
 }
}