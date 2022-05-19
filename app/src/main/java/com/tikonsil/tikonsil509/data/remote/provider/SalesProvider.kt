package com.tikonsil.tikonsil509.data.remote.provider

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.tikonsil.tikonsil509.domain.model.Sales

/** * Created by ISMOY BELIZAIRE on 01/05/2022. */
class SalesProvider {
 var mDatabase:DatabaseReference?= FirebaseDatabase.getInstance().reference.child("Sales")
 fun create(sales: Sales?): Task<Void?>? {
  val map: MutableMap<String?, Any?>? = HashMap()
  map?.apply {
   put("idUser", sales?.idUser)
   put("firstname", sales?.firstname)
   put("lastname", sales?.lastname)
   put("email", sales?.email)
   put("role", sales?.role)
   put("typerecharge", sales?.typerecharge)
   put("phone", sales?.phone)
   put("date", sales?.date)
   put("country", sales?.country)
   put("subtotal", sales?.subtotal)
   put("description", sales?.description)
  }
  return mDatabase?.push()?.setValue(map)
 }

}