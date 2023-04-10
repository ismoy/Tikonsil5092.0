package com.tikonsil.tikonsil509.domain.model

data class SendRechargeResponse(
    val balance: Float,
    val destination: String,
    val error_code: Int,
    val recharge_id: Int,
    val reference_code: String,
    val status: String,
    val message: String,
)
