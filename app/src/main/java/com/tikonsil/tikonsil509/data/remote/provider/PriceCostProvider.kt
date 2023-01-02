package com.tikonsil.tikonsil509.data.remote.provider

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PriceCostProvider {

    private var mDatabase:DatabaseReference =FirebaseDatabase.getInstance().reference.child("PriceCountryInnoverit")

    fun getPriceCost():DatabaseReference{
        return mDatabase
    }
}