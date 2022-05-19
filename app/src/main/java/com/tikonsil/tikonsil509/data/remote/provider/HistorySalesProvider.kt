package com.tikonsil.tikonsil509.data.remote.provider

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class HistorySalesProvider {
    var mDatabase: DatabaseReference?= FirebaseDatabase.getInstance().reference.child("Sales")
    suspend fun getHistorySales(idUser: String): Query?{
        val query: Query? =mDatabase?.orderByChild("idUser")?.equalTo(idUser)
        return query

    }
}