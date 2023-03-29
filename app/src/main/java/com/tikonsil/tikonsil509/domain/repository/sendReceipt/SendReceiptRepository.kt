package com.tikonsil.tikonsil509.domain.repository.sendReceipt

import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.Headers
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.sendReceipt.SendReceipt
import com.tikonsil.tikonsil509.domain.model.sendReceipt.SendReceiptResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class SendReceiptRepository {
    suspend fun sendReceipt(sendReceipt: SendReceipt):Response<SendReceiptResponse> = withContext(Dispatchers.IO){
        val _tikonsil509 =
            RetrofitInstance(FirebaseApi.getFSApis().base_url_tikonsil).tikonsilApi
        return@withContext _tikonsil509.sendReceipt(
            Headers.getHeaderTikonsil509(),FirebaseApi.getFSApis().
        endPoint_send_receipt,sendReceipt)
    }
}