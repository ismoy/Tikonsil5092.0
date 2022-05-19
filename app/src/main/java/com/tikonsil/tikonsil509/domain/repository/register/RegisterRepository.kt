package com.tikonsil.tikonsil509.domain.repository.register

import android.app.Application
import com.tikonsil.tikonsil509.data.remote.api.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.Users
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
class RegisterRepository(private var application: Application) {

    init {
        this.application = application
    }
    suspend fun register(uiduser: String?, users: Users): Response<Users> {
        return RetrofitInstance.tikonsilApi.registerClient(uiduser!!, users)
    }
}