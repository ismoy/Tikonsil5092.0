package com.tikonsil.tikonsil509.presentation.FCM

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.FCMBody
import com.tikonsil.tikonsil509.domain.model.FCMResponse
import com.tikonsil.tikonsil509.domain.repository.FCM.SendNotificationRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 12/05/2022. */
class SendNotificationViewModel(val repository: SendNotificationRepository):ViewModel() {
    val ResponseFCM: MutableLiveData<Call<FCMResponse?>> by lazy { MutableLiveData() }

    @SuppressLint("NullSafeMutableLiveData")
    fun sendNotification(body: FCMBody?){
        viewModelScope.launch {
            /*val response = repository.sendNotification(body)
            ResponseFCM.value = response*/
        }
    }
}