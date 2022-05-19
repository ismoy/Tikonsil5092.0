package com.tikonsil.tikonsil509.presentation.countryprices

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.CountryPrice
import com.tikonsil.tikonsil509.domain.repository.countryprices.CountryPricesRepository
import kotlinx.coroutines.launch
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 28/04/2022. */
class CountryPricesViewModel(private val repository: CountryPricesRepository) :
    ViewModel() {
    val myResponseGetCountryPrice: MutableLiveData<Response<CountryPrice>> by lazy { MutableLiveData() }

    fun getCountryPrice() {
        viewModelScope.launch {
            val response = repository.getCountryPrice()
            myResponseGetCountryPrice.value = response
        }
    }
}