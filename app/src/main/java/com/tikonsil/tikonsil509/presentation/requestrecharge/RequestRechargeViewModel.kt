package com.tikonsil.tikonsil509.presentation.requestrecharge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.tikonsil.tikonsil509.data.local.db.UsersDatabase
import com.tikonsil.tikonsil509.data.local.entity.PriceRechargeAccountMaster
import com.tikonsil.tikonsil509.domain.model.PriceRechargeAccountMasterResponse
import com.tikonsil.tikonsil509.domain.requestRecharge.RequestRechargeRepository
import kotlinx.coroutines.launch
import retrofit2.Response

/** * Created by ISMOY BELIZAIRE on 17/05/2022. */
class RequestRechargeViewModel(application: Application):AndroidViewModel(application) {

    private val repository:RequestRechargeRepository
    private val _resultListRecharge:MutableLiveData<Response<PriceRechargeAccountMasterResponse>> by lazy { MutableLiveData() }
    val resultListRecharge:LiveData<Response<PriceRechargeAccountMasterResponse>> = _resultListRecharge
    private val _retrieveLocaleData:MutableLiveData<List<PriceRechargeAccountMaster>> by lazy { MutableLiveData() }
    val retrieveLocaleData:LiveData<List<PriceRechargeAccountMaster>> = _retrieveLocaleData

    init {
        val priceRechargeAccountMasterDao =UsersDatabase.getDatabase(application).priceRechargeAccountMasterDao()
        repository = RequestRechargeRepository(priceRechargeAccountMasterDao)
    }
    fun getListPriceRechargeAccountMaster(){
        viewModelScope.launch {
            val result = repository.getListPriceRechargeAccountMaster()
            _resultListRecharge.value = result
        }
    }

    fun insertPrices(priceRechargeAccountMaster: PriceRechargeAccountMaster) {
        viewModelScope.launch {
            repository.insertPrices(priceRechargeAccountMaster)
        }
    }

    fun getAllPrices(){
        viewModelScope.launch {
            val result = repository.getAllPrices()
            _retrieveLocaleData.value = result
        }
    }

    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteAllPrices()
        }
    }
}