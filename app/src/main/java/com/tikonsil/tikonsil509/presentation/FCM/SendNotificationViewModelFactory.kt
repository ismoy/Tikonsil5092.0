package com.tikonsil.tikonsil509.presentation.FCM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tikonsil.tikonsil509.domain.repository.FCM.SendNotificationRepository
import com.tikonsil.tikonsil509.presentation.home.UserViewModel

/** * Created by ISMOY BELIZAIRE on 12/05/2022. */
class SendNotificationViewModelFactory (val repository: SendNotificationRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SendNotificationViewModel(repository) as T
    }
}