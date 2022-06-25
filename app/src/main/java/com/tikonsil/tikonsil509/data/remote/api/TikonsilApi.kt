package com.tikonsil.tikonsil509.data.remote.api

import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.domain.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
interface TikonsilApi {
 @PATCH("Clients/{uidUser}.json")
 suspend fun registerClient(@Path("uidUser") uidUser:String,@Body param:Users):Response<Users>

 @GET("Clients/{uidUser}.json")
 suspend fun getOnlyUser(@Path("uidUser") uidUser:String):Response<Users>

 @POST("Sales.json")
 suspend fun Sales(@Body sales: Sales):Response<Sales>

 @POST("Notification.json")
 suspend fun saveNotification(@Body saveNotification: SaveNotification):Response<SaveNotification>

 @GET("CountryPrice.json")
 suspend fun getCountryPrice():Response<CountryPrice>

 @GET("BonusUser.json")
 suspend fun getBounusUser():Response<BonusUser>

}