package com.tikonsil.tikonsil509.presentation.historysales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tikonsil.tikonsil509.domain.repository.historysales.HistorySalesRepository

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class HistorySalesViewModelProvider(private val repository: HistorySalesRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return HistorySalesViewModel(repository) as T
    }
}