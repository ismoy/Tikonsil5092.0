package com.tikonsil.tikonsil509.domain.model

data class CostInnoverit(
    val country:String,
    val operatorName:String,
    val priceSale:String,
    val id_product:String
){
    override fun toString(): String {
        return priceSale
    }
}

