package com.tikonsil.tikonsil509.domain.repository.sendrecharge

import com.tikonsil.tikonsil509.data.remote.api.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.Sales
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class SendRechargeRepository {
    suspend fun Sales(sales: Sales): Response<Sales> {
        return RetrofitInstance.tikonsilApi.Sales(sales)
    }
}