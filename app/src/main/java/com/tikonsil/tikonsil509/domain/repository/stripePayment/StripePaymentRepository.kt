package com.tikonsil.tikonsil509.domain.repository.stripePayment

import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.Headers
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.stripePayment.StripePayment
import com.tikonsil.tikonsil509.domain.model.stripePayment.StripePaymentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class StripePaymentRepository {
    suspend fun  createPayment(stripePayment: StripePayment):Response<StripePaymentResponse> =
        withContext(Dispatchers.IO){
            val _tikonsilApi = RetrofitInstance(FirebaseApi.getFSApis().base_url_tikonsil).tikonsilApi
            return@withContext _tikonsilApi.stripePayment(Headers.getHeaderTikonsil509(),FirebaseApi.
            getFSApis().end_point_payment_stripe,stripePayment)
        }
}