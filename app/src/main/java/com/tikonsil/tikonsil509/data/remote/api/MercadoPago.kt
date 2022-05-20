package com.tikonsil.tikonsil509.data.remote.api

import com.google.gson.JsonObject
import com.tikonsil.tikonsil509.domain.model.MercadoPagoCardTokenBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/** * Created by ISMOY BELIZAIRE on 19/05/2022. */
interface MercadoPago {
    @POST("v1/card_tokens?public_key=TEST-d6b8c039-4914-46c8-9093-471a1bcd2cda")
    suspend fun createCardToken(@Body body:MercadoPagoCardTokenBody):Response<JsonObject>

    @POST("v1/card_tokens?public_key=TEST-d6b8c039-4914-46c8-9093-471a1bcd2cda")
     fun createCardToken2(@Body body:MercadoPagoCardTokenBody):Call<JsonObject>
}