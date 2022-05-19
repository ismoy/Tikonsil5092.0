package com.tikonsil.tikonsil509.domain.model

import com.google.gson.annotations.SerializedName

/** * Created by ISMOY BELIZAIRE on 27/04/2022. */
data class Sales(
    @SerializedName("idUser") val idUser: String? = null,
    @SerializedName("firstname") val firstname: String? = null,
    @SerializedName("lastname") val lastname: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("role") val role: Int = 0,
    @SerializedName("typerecharge") val typerecharge: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("codecountry") val codecountry:String?=null,
    @SerializedName("subtotal") val subtotal: String? = null,
    @SerializedName("description") val description: String? = null
)