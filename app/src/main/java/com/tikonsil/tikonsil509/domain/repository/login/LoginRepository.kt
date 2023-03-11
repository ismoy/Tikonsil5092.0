package com.tikonsil.tikonsil509.domain.repository.login

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** * Created by ISMOY BELIZAIRE on 26/04/2022. */
class LoginRepository {
    private val authProvider by lazy { AuthProvider() }

  suspend fun login(email:String,password:String): Task<AuthResult> = withContext(Dispatchers.IO){
       return@withContext authProvider.login(email, password)
    }


}
