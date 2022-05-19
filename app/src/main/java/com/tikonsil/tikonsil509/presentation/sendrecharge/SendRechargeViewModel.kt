package com.tikonsil.tikonsil509.presentation.sendrecharge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.Sales
import com.tikonsil.tikonsil509.domain.repository.sendrecharge.SendRechargeRepository
import kotlinx.coroutines.launch
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class SendRechargeViewModel(private val repository: SendRechargeRepository) :ViewModel(){
    val myResponsesales: MutableLiveData<Response<Sales>> by lazy { MutableLiveData() }
    fun Sales(sales: Sales) {
        viewModelScope.launch {
            val response = repository.Sales(sales)
            myResponsesales.value = response
        }
    }
}