package com.tikonsil.tikonsil509.presentation.sendReceipt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.sendReceipt.SendReceipt
import com.tikonsil.tikonsil509.domain.model.sendReceipt.SendReceiptResponse
import com.tikonsil.tikonsil509.domain.repository.sendReceipt.SendReceiptRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class SendReceiptViewModel:ViewModel() {

    val repository :SendReceiptRepository = SendReceiptRepository()
    private val _responseSendReceipt:MutableLiveData<Response<SendReceiptResponse>> by lazy { MutableLiveData() }
    val responseSendReceipt:LiveData<Response<SendReceiptResponse>> = _responseSendReceipt


    fun sendReceipt(sendReceipt: SendReceipt){
        viewModelScope.launch {
            val result = repository.sendReceipt(sendReceipt)
            _responseSendReceipt.value = result
        }
    }
}