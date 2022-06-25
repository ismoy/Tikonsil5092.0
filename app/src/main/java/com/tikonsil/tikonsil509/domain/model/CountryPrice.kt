package com.tikonsil.tikonsil509.domain.model

import com.google.gson.annotations.SerializedName

/** * Created by ISMOY BELIZAIRE on 29/04/2022. */
data class CountryPrice(
 @SerializedName("pricetopuphaiti") val pricetopuphaiti: Float? =null,
 @SerializedName("pricemoncashhaiti") val pricemoncashhaiti:Float,
 @SerializedName ("pricecuba") val pricecuba:Float,
 @SerializedName("pricemexico") val pricemexico:Float,
 @SerializedName("pricepanama") val pricepanama:Float,
 @SerializedName("priceRD") val priceRD:Float,
 @SerializedName("pricebrasil") val pricebrasil:Float,
 @SerializedName("pricechile") val pricechile:Float,
 @SerializedName("priceus") val priceus:Float,
 @SerializedName("pricelapoulahaiti") val pricelapoulahaiti:Float,
 @SerializedName("pricenatcashhaiti") val pricenatcashhaiti:Float

)
