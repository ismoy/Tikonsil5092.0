package com.tikonsil.tikonsil509.data.remote.provider

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tikonsil.tikonsil509.domain.model.Users
import java.util.HashMap

/** * Created by ISMOY BELIZAIRE on 27/04/2022. */
class UserProvider {
 var mDatabase:DatabaseReference?=FirebaseDatabase.getInstance().reference.child("Clients")

 fun updateTopup(idUser: String? , saldotopup: Float): Task<Void?>? {
  val map: MutableMap<String?, Any?> = HashMap()
  map["soldtopup"] = saldotopup
  return idUser?.let { mDatabase?.child(it)?.updateChildren(map) }
 }

 fun updateMoncash(idUser: String?, saldomoncash: Int): Task<Void?>? {
  val map: MutableMap<String?, Any?> = HashMap()
  map["soldmoncash"] = saldomoncash
  return idUser?.let { mDatabase?.child(it)?.updateChildren(map) }
 }

 fun updateNatCash(idUser: String?, saldonatcash: Int): Task<Void?>? {
  val map: MutableMap<String?, Any?> = HashMap()
  map["soldnatcash"] = saldonatcash
  return idUser?.let { mDatabase?.child(it)?.updateChildren(map) }
 }

 fun updateLapoula(idUser: String?, saldolapoula: Int): Task<Void?>? {
  val map: MutableMap<String?, Any?> = HashMap()
  map["soldlapoula"] = saldolapoula
  return idUser?.let { mDatabase?.child(it)?.updateChildren(map) }
 }

 fun getUser(): DatabaseReference? {
  return mDatabase
 }

 fun updateImage(user: Users?): Task<Void>? {
  val map: MutableMap<String?, Any?> = HashMap()
  map.apply {
   put("image", user?.image)
  }
  return user?.id?.let {
   mDatabase?.child(it)?.updateChildren(map) }
 }
}