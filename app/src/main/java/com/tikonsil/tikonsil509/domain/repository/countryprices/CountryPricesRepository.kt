package com.tikonsil.tikonsil509.domain.repository.countryprices

import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.CountryPrice
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 28/04/2022. */
class CountryPricesRepository {
    suspend fun getCountryPrice(): Response<CountryPrice> {
        val _tikonsilApi= RetrofitInstance(FirebaseApi.getFSApis().base_url_firebase_instance).tikonsilApi
        return _tikonsilApi.getCountryPrice(FirebaseApi.getFSApis().end_point_country_price)
    }
}