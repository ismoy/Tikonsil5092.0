package com.tikonsil.tikonsil509.data.remote.provider

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.tikonsil.tikonsil509.domain.model.Token

/** * Created by ISMOY BELIZAIRE on 12/05/2022. */
class TokenProvider {
    var mDatabaseReference: DatabaseReference? = FirebaseDatabase.getInstance().reference.child("Tokens")

    fun createToken(idUser:String?){
        if (idUser ==null) return
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task->
                val token = Token(task.result)
                mDatabaseReference?.child(idUser)?.setValue(token)
            }
    }

    fun getToken(idUser: String?): DatabaseReference {
        return mDatabaseReference!!.child(idUser!!)
    }

}