package com.tikonsil.tikonsil509.presentation.lastsales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.LastSales
import com.tikonsil.tikonsil509.domain.repository.lastsales.LastSalesRepository
import kotlinx.coroutines.launch

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class LastSalesViewModel(private val repository: LastSalesRepository):ViewModel() {
    var isSnapShotExist =MutableLiveData<Boolean>()
    init {
        isSnapShotExist =repository.isSnapShotExist
    }
    fun getLastSales(idUser:String): LiveData<MutableList<LastSales>> {
        val mutabledata = MutableLiveData<MutableList<LastSales>>()
        viewModelScope.launch {
            repository.getLastSales(idUser).observeForever{
                mutabledata.value = it
            }
        }
        return mutabledata
    }

}