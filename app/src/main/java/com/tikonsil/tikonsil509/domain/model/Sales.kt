package com.tikonsil.tikonsil509.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** * Created by ISMOY BELIZAIRE on 27/04/2022. */
@Parcelize
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
    @SerializedName("description") val description: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("status") val status: Int=0,
    @SerializedName("id_product") val idProduct:Int,
    @SerializedName("salesPrice") val salesPrice:String,
    @SerializedName("image") val image:String

):Parcelable