package com.tikonsil.tikonsil509.domain.model.mercadoPago

import com.google.gson.annotations.SerializedName

data class Cardholder(
    @SerializedName("name") val name: String
){
    override fun toString(): String {
        return "CardHolder(name='$name')"
    }
}
