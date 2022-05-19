package com.tikonsil.tikonsil509.domain.repository.profile

import android.app.Application
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tikonsil.tikonsil509.data.remote.api.RetrofitInstance
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.data.remote.provider.UserProvider
import com.tikonsil.tikonsil509.domain.model.Users
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 27/04/2022. */
class ProfileRepository {
    suspend fun getOnlyUser(uidUser:String):Response<Users> {
    return RetrofitInstance.tikonsilApi.getOnlyUser(uidUser)
    }
}