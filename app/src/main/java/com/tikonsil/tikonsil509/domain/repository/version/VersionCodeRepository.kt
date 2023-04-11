package com.tikonsil.tikonsil509.domain.repository.version

import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.VersionsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class VersionCodeRepository {

    suspend fun getVersionCode():Response<VersionsResponse> =
        withContext(Dispatchers.IO){
            val tikonsilApi = RetrofitInstance(FirebaseApi.getFSApis().base_url_firebase_instance).tikonsilApi
           return@withContext tikonsilApi.getVersion(FirebaseApi.getFSApis().end_point_version)
        }
}