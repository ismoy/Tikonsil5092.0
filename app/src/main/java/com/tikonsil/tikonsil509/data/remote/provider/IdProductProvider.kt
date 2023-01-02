package com.tikonsil.tikonsil509.data.remote.provider

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class IdProductProvider {

    var mDatabase: DatabaseReference?= FirebaseDatabase.getInstance().reference.child("IdProductCountryInnoverit")

    fun getIdProductSelected(): DatabaseReference? {
        return mDatabase
    }
}