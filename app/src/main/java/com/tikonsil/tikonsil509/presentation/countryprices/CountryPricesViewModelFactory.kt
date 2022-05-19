package com.tikonsil.tikonsil509.presentation.countryprices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tikonsil.tikonsil509.domain.repository.countryprices.CountryPricesRepository

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
class CountryPricesViewModelFactory(private val repository: CountryPricesRepository):ViewModelProvider.Factory {
 override fun <T : ViewModel> create(modelClass: Class<T>): T {
  return CountryPricesViewModel(repository) as T
 }

}