package com.tikonsil.tikonsil509.domain.repository.login

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/** * Created by ISMOY BELIZAIRE on 26/04/2022. */
class LoginRepository {
    var fAuth: FirebaseAuth? = null
    var application = Application()
    private val authProvider = AuthProvider()
    init {
        fAuth = FirebaseAuth.getInstance()
        fAuth?.currentUser
        this.application =application
    }

   suspend fun login(email:String,password:String): Task<AuthResult> {
       return authProvider.login(email, password)
    }


}
