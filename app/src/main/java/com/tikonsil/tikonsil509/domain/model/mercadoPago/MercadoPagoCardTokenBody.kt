package com.tikonsil.tikonsil509.domain.model.mercadoPago

import com.google.gson.annotations.SerializedName

data class MercadoPagoCardTokenBody(
    @SerializedName("security_code") val securityCode: String ,
    @SerializedName("expiration_year") val expirationYear: String ,
    @SerializedName("expiration_month") val expirationMonth: Int ,
    @SerializedName("card_number") val cardNumber: String ,
    @SerializedName("cardholder") val cardHolder: Cardholder
){
    override fun toString(): String {
        return "MercadoPagoCardTokenBody(securityCode='$securityCode', expirationYear='$expirationYear', expirationMonth=$expirationMonth, cardNumber='$cardNumber', cardHolder=$cardHolder)"
    }
}
