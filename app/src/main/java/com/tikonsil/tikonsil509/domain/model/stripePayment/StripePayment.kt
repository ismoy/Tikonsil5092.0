package com.tikonsil.tikonsil509.domain.model.stripePayment

data class StripePayment(
    val card_number: String ,
    val card_month: String ,
    val card_year: String ,
    val card_cvv: String ,
    val amount: String
)
