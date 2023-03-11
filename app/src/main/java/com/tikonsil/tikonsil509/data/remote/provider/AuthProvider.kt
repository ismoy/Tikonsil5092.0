package com.tikonsil.tikonsil509.data.remote.provider

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.tikonsil.tikonsil509.R

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
class AuthProvider {
 private var auth:FirebaseAuth = FirebaseAuth.getInstance()

  fun register(email:String?,password:String?):Task<AuthResult>{
  return auth.createUserWithEmailAndPassword(email!!,password!!)
 }
  fun getId():String?{
  return auth.currentUser?.uid
 }
 fun isEmailVerified(): Boolean? {
  return auth.currentUser?.isEmailVerified
 }
 fun logout() {
  auth.signOut()
 }

  fun language(context: Context) {
  auth.setLanguageCode(context.getString(R.string.lang_email))
 }
  fun login(email:String, password: String): Task<AuthResult> {
  return auth.signInWithEmailAndPassword(email, password)
 }

 fun resetPassword(email: String,context: Context): Task<Void> {
  language(context)
  return auth.sendPasswordResetEmail(email)
 }

 fun existSession(): Boolean {
  var exist = false
  if (auth.currentUser != null) {
   exist = true
  }
  return exist
 }

}