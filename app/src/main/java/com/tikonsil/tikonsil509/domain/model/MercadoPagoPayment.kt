package com.tikonsil.tikonsil509.domain.model

import com.google.gson.annotations.SerializedName
import com.optic.kotlinudemydelivery.models.Payer

class MercadoPagoPayment(
    @SerializedName("order") val order: Sales,
    @SerializedName("token") val token: String,
    @SerializedName("description") val description: String,
    @SerializedName("payment_method_id") val paymentMethodId: String,
    @SerializedName("payment_type_id") val paymentTypeId: String,
    @SerializedName("issuer_id") val issuerId: String,
    @SerializedName("payer") val payer: Payer,
    @SerializedName("transaction_amount") val transactionAmount: Double,
    @SerializedName("installments") val installments: Int,


    ) {
}