package com.tikonsil.tikonsil509.domain.repository.fcm

import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.Headers
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.PushNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 12/05/2022. */
class SendNotificationRepository {

   suspend fun sendNotification(pushNotification: PushNotification): Response<PushNotification> =
       withContext(Dispatchers.IO) {
       val _tikonsil509 =RetrofitInstance(FirebaseApi.getFSApis().fcm.base_url_fcm).tikonsilApi
        return@withContext _tikonsil509 .postNotification(FirebaseApi.getFSApis().fcm.end_point_fcm,pushNotification,Headers.getHeaderFcm())
    }
}