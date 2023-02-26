package com.tikonsil.tikonsil509.presentation.fcm

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.RemoteMessage.Notification
import com.tikonsil.tikonsil509.domain.model.FCMBody
import com.tikonsil.tikonsil509.domain.model.FCMResponse
import com.tikonsil.tikonsil509.domain.model.PushNotification
import com.tikonsil.tikonsil509.domain.repository.fcm.SendNotificationRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 12/05/2022. */
class SendNotificationViewModel():ViewModel() {

    private val repository:SendNotificationRepository = SendNotificationRepository()
    val _sendNotification: MutableLiveData<Response<PushNotification>> by lazy { MutableLiveData() }
    val sendNotification: LiveData<Response<PushNotification>> =_sendNotification

    fun sendNotification(pushNotification: PushNotification){
        viewModelScope.launch {
            val result = repository.sendNotification(pushNotification)
            _sendNotification.value = result
        }
    }
}