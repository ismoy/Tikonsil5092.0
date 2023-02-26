package com.tikonsil.tikonsil509.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PriceRechargeAccountMaster(
    @PrimaryKey(autoGenerate = true)
    val price1: Int ,
    val price10: Int ,
    val price11: Int ,
    val price12: Int ,
    val price13: Int ,
    val price14: Int ,
    val price15: Int ,
    val price16: Int ,
    val price17: Int ,
    val price18: Int ,
    val price19: Int ,
    val price2: Int ,
    val price3: Int ,
    val price4: Int ,
    val price5: Int ,
    val price6: Int ,
    val price7: Int ,
    val price8: Int ,
    val price9: Int
)
