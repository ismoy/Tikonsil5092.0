package com.tikonsil.tikonsil509.presentation.sendrecharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tikonsil.tikonsil509.domain.repository.sendrecharge.SendRechargeRepository

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class SendRechargeViewModelProvider(private val repository: SendRechargeRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SendRechargeViewModel(repository) as T
    }
}