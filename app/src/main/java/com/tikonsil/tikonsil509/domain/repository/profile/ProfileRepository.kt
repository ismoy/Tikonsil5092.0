package com.tikonsil.tikonsil509.domain.repository.profile

import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.Users
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 27/04/2022. */
class ProfileRepository {
    suspend fun getOnlyUser(uidUser:String):Response<Users> {
        val _tikonsilApi= RetrofitInstance(FirebaseApi.getFSApis().base_url_firebase_instance).tikonsilApi
    return _tikonsilApi.getOnlyUser(uidUser)
    }
}