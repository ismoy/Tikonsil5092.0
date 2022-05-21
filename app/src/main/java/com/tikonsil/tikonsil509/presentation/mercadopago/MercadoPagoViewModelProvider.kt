package com.tikonsil.tikonsil509.presentation.mercadopago

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tikonsil.tikonsil509.domain.repository.mercadopago.MercadoPagoRepository

/** * Created by ISMOY BELIZAIRE on 19/05/2022. */
class MercadoPagoViewModelProvider(private val repository:MercadoPagoRepository):ViewModelProvider.Factory {
 override fun <T : ViewModel> create(modelClass: Class<T>): T {
return MercadoPagoViewModel(repository) as T
 }
}