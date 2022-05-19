package com.tikonsil.tikonsil509.domain.repository.savenotification

import com.tikonsil.tikonsil509.data.remote.api.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.SaveNotification
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class SaveNotificationRepository {
    suspend fun saveNotification(saveNotification: SaveNotification): Response<SaveNotification> {
        return RetrofitInstance.tikonsilApi.saveNotification(saveNotification)
    }
}