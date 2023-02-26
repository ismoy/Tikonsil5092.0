package com.tikonsil.tikonsil509.data.remote.retrofitInstance

import com.tikonsil.tikonsil509.data.remote.api.TikonsilApi
import com.tikonsil.tikonsil509.utils.constants.Constant.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
class RetrofitInstance(var url_base: String ) {
 private val retrofit by lazy {
  Retrofit.Builder()
   .baseUrl(url_base)
   .addConverterFactory(GsonConverterFactory.create())
   .client(getClientHttp())
   .build()
 }
 val tikonsilApi: TikonsilApi by lazy {
  retrofit.create(TikonsilApi::class.java)
 }

 private fun getClientHttp(): OkHttpClient {
  val logging = HttpLoggingInterceptor()
  logging.level = HttpLoggingInterceptor.Level.BODY
  return OkHttpClient()
   .newBuilder()
   .addInterceptor(logging).build()
 }
}