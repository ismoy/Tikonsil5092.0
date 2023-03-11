package com.tikonsil.tikonsil509.domain.repository.requestRecharge

import com.tikonsil.tikonsil509.data.local.dao.PriceRechargeAccountMasterDao
import com.tikonsil.tikonsil509.data.local.entity.PriceRechargeAccountMaster
import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import com.tikonsil.tikonsil509.data.remote.retrofitInstance.RetrofitInstance
import com.tikonsil.tikonsil509.domain.model.PriceRechargeAccountMasterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class RequestRechargeRepository(private val priceRechargeAccountMasterDao: PriceRechargeAccountMasterDao) {

    suspend fun getListPriceRechargeAccountMaster(): Response<PriceRechargeAccountMasterResponse> = withContext(Dispatchers.IO) {
        val _tikonsil509 =RetrofitInstance(FirebaseApi.getFSApis().base_url_firebase_instance).tikonsilApi
        return@withContext _tikonsil509.getListPriceRechargeAccountMaster(FirebaseApi.getFSApis().end_point_list_price_recharge_account_master)
    }

    suspend fun insertPrices(priceRechargeAccountMaster: PriceRechargeAccountMaster) = withContext(Dispatchers.IO){
       return@withContext priceRechargeAccountMasterDao.insertPrices(priceRechargeAccountMaster)
    }

    suspend fun getAllPrices():List<PriceRechargeAccountMaster> = withContext(Dispatchers.IO){
        return@withContext priceRechargeAccountMasterDao.getAllPrices()
    }

    suspend fun deleteAllPrices() = withContext(Dispatchers.IO){
        return@withContext priceRechargeAccountMasterDao.deleteAllPrices()
    }
}