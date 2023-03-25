package com.tikonsil.tikonsil509.data.remote.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tikonsil.tikonsil509.data.local.entity.PriceRechargeAccountMaster
import com.tikonsil.tikonsil509.domain.model.*
import com.tikonsil.tikonsil509.domain.model.mercadoPago.MercadoPagoCardTokenBody
import com.tikonsil.tikonsil509.domain.model.mercadoPago.Payment
import com.tikonsil.tikonsil509.domain.model.stripePayment.StripePayment
import com.tikonsil.tikonsil509.domain.model.stripePayment.StripePaymentResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
interface TikonsilApi {
    @PATCH("Clients/{uidUser}.json")
    suspend fun registerClient(
        @Path(
            "uidUser"
        ) uidUser: String , @Body param: Users
    ): Response<Users>

    @GET("Clients/{uidUser}.json")
    suspend fun getOnlyUser(
        @Path("uidUser") uidUser: String
    ): Response<Users>

    @PATCH("Sales/{uidUserCodeProduct}.json")
    suspend fun sales(
        @Path("uidUserCodeProduct")uidUserCodeProduct:String,
        @Body sales: Sales
    ): Response<Sales>

    @POST
    suspend fun salesWithErrorInnoverit(
        @Url url: String ,
        @Body sales: Sales
    ): Response<Sales>

    @POST
    suspend fun saveNotification(
        @Url url: String ,
        @Body saveNotification: SaveNotification
    ): Response<SaveNotification>

    @GET
    suspend fun getCountryPrice(
        @Url url: String
    ): Response<CountryPrice>

    @POST
    fun sendProduct(
        @HeaderMap authorization: Map<String , String>,
        @Url url: String ,
        @Body sendRechargeProduct: SendRechargeProduct
    ): Call<SendRechargeResponse>

    @POST
    suspend fun postNotification(
        @Url url: String ,
        @Body notification: PushNotification,
        @HeaderMap authorization: Map<String, String>
    ): Response<PushNotification>

    @POST
    suspend fun createCardToken(
        @Url url: String ,
        @Body body: MercadoPagoCardTokenBody
    ): Response<JsonObject>

    @GET
    suspend fun getInstallments(
        @Url url: String ,
        @Query("bin") bin: String ,
        @Query("amount") amount: String
    ): Response<JsonArray>
    @POST
    suspend fun createPayWithMercadoPago(
        @HeaderMap authorization: Map<String, String>,
        @Url url: String ,
        @Body payment: Payment
    ): Response<JsonObject>

    @GET
    suspend fun getListPriceRechargeAccountMaster(
        @Url url:String
    ):Response<PriceRechargeAccountMasterResponse>

    @POST
    suspend fun stripePayment(
        @HeaderMap authorization: Map<String,String>,
        @Url url: String,
        @Body stripePayment: StripePayment
    ):Response<StripePaymentResponse>
}