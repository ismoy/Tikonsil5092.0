package com.tikonsil.tikonsil509.data.remote.api

import com.tikonsil.tikonsil509.utils.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceFCM {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_FCM)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val notificationAPI by lazy {
        retrofit.create(NotificationAPI::class.java)
    }
}