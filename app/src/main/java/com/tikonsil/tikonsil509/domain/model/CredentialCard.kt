package com.tikonsil.tikonsil509.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CredentialCard (
    val cardNumber:String,
    val cardMonth:String,
    val cardYear:String,
    val cardCvv:String
    ):Parcelable