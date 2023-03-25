package com.tikonsil.tikonsil509.presentation.stripePayment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.stripePayment.StripePayment
import com.tikonsil.tikonsil509.domain.model.stripePayment.StripePaymentResponse
import com.tikonsil.tikonsil509.domain.repository.stripePayment.StripePaymentRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class StripePaymentViewModel:ViewModel() {

    private val repository:StripePaymentRepository = StripePaymentRepository()
    private val _responsePaymentStripe:MutableLiveData<Response<StripePaymentResponse>> by lazy { MutableLiveData() }
    val responsePaymentStripe:LiveData<Response<StripePaymentResponse>> = _responsePaymentStripe
    private val _isLoading:MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val isLoading : LiveData<Boolean> =_isLoading
    fun createPayment(stripePayment: StripePayment){
        viewModelScope.launch {
            _isLoading.value =true
            val result = repository.createPayment(stripePayment)
            _responsePaymentStripe.value = result
        }
    }
}