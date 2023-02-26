package com.tikonsil.tikonsil509.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MercadoPagoCredencials (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val card_token: String,
    val first_six_digits:String,
    val card_holder_name:String,
    val last_four_digits:String
)