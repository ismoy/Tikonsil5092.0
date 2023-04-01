package com.tikonsil.tikonsil509.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
   @PrimaryKey(autoGenerate = false) val  id:Long,
   val nameProduct:String,
   val amount:String,
   val email:String,
   val companyName:String,
   val phoneNumber:String,
   val receiverAmount:String,
   val idProduct:Int,
   val userId:String,
   val firstName:String,
   val lastName:String,
   val role:Int,
   val tokenUser:String,
   val tokenAdmin:String,
   val date:String,
   val status:Int,
   val countryName:String,
   val imageUrl:String,
   val soldTopUp:Float,
   val subTotal:String,
   val currently:String

)