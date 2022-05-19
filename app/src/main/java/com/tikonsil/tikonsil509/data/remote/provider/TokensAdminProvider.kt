package com.tikonsil.tikonsil509.data.remote.provider

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.tikonsil.tikonsil509.domain.model.Token

/** * Created by ISMOY BELIZAIRE on 12/05/2022. */
class TokensAdminProvider {
 var mDatabaseReference: DatabaseReference? = FirebaseDatabase.getInstance().reference.child("TokensAdmin")

   fun getToken(): DatabaseReference? {
   return mDatabaseReference
  }



}