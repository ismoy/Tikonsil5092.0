package com.tikonsil.tikonsil509.domain.repository.countryprices

import com.tikonsil.tikonsil509.data.remote.api.RetrofitInstance
import com.tikonsil.tikonsil509.data.remote.provider.SalesProvider
import com.tikonsil.tikonsil509.domain.model.CountryPrice
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 28/04/2022. */
class CountryPricesRepository() {
 suspend fun getCountryPrice(): Response<CountryPrice> {
  return RetrofitInstance.tikonsilApi.getCountryPrice()
 }
}