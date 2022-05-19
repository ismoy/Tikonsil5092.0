package com.tikonsil.tikonsil509.presentation.savenotification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tikonsil.tikonsil509.domain.repository.savenotification.SaveNotificationRepository

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class SaveNotificationViewModelProvider(private val repository: SaveNotificationRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return SaveNotificationViewModel(repository) as T
    }
}