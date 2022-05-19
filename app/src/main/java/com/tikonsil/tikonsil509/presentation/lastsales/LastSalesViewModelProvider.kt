package com.tikonsil.tikonsil509.presentation.lastsales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tikonsil.tikonsil509.domain.repository.lastsales.LastSalesRepository

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class LastSalesViewModelProvider(private val repository: LastSalesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LastSalesViewModel(repository) as T
    }
}