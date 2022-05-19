package com.tikonsil.tikonsil509.domain.repository.home

import com.tikonsil.tikonsil509.data.remote.api.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.Users
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 27/04/2022. */
class UsersRepository {
    suspend fun getOnlyUser(uidUser:String):Response<Users> {
    return RetrofitInstance.tikonsilApi.getOnlyUser(uidUser)
    }


}