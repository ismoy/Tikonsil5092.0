package com.tikonsil.tikonsil509.presentation.savenotification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.SaveNotification
import com.tikonsil.tikonsil509.domain.repository.savenotification.SaveNotificationRepository
import kotlinx.coroutines.launch
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class SaveNotificationViewModel(private val repository: SaveNotificationRepository):ViewModel() {
    val myResponsesavenotification: MutableLiveData<Response<SaveNotification>> by lazy { MutableLiveData() }
    fun saveNotification(saveNotification: SaveNotification) {
        viewModelScope.launch {
            val response = repository.saveNotification(saveNotification)
            myResponsesavenotification.value = response
        }
    }
}