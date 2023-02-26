package com.tikonsil.tikonsil509.domain.repository.savenotification

import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.SaveNotification
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class SaveNotificationRepository {
    suspend fun saveNotification(saveNotification: SaveNotification): Response<SaveNotification> {
        val _tikonsilApi= RetrofitInstance(FirebaseApi.getFSApis().base_url_firebase_instance).tikonsilApi
        return _tikonsilApi.saveNotification(FirebaseApi.getFSApis().end_point_save_notification,saveNotification)
    }
}