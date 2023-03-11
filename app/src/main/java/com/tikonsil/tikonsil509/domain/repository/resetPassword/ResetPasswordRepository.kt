package com.tikonsil.tikonsil509.domain.repository.resetPassword

import android.content.Context
import com.google.android.gms.tasks.Task
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResetPasswordRepository {

    private val mAuth by lazy { AuthProvider() }

    suspend fun resetPassword(email:String,context: Context):Task<Void> = withContext(Dispatchers.IO){
        return@withContext  mAuth.resetPassword(email,context)

    }

}