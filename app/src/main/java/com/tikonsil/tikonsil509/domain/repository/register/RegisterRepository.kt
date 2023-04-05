package com.tikonsil.tikonsil509.domain.repository.register

import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.Users
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
class RegisterRepository {
    suspend fun register(uiduser: String?, users: Users): Response<Users> {
        return RetrofitInstance(FirebaseApi.getFSApis().base_url_firebase_instance).tikonsilApi.registerClient(uiduser!!,users)
    }
}