package com.tikonsil.tikonsil509.domain.model.mercadoPago

data class Payment(
    val transaction_amount:String,
    val token:String,
    val installments:Int,
    val payment_method_id:String,
    val issuer_id:Int,
    val email:String
)
