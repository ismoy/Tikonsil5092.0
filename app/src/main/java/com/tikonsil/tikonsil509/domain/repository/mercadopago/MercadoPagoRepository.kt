package com.tikonsil.tikonsil509.domain.repository.mercadopago

import com.google.gson.JsonObject
import com.tikonsil.tikonsil509.data.remote.api.MercadoPagoInstance
import com.tikonsil.tikonsil509.domain.model.MercadoPagoCardTokenBody
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 19/05/2022. */
class MercadoPagoRepository {
 suspend fun createToken(mercadoPagoCardTokenBody:MercadoPagoCardTokenBody): Response<JsonObject> {
  return MercadoPagoInstance.mercadopagoapi.createCardToken(mercadoPagoCardTokenBody)
 }
}