package com.tikonsil.tikonsil509.presentation.historysales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.Sales
import com.tikonsil.tikonsil509.domain.repository.historysales.HistorySalesRepository
import kotlinx.coroutines.launch

/** * Created by ISMOY BELIZAIRE on 16/05/2022. */
class HistorySalesViewModel(private val repository: HistorySalesRepository):ViewModel() {

    var isExistSnapShot = MutableLiveData<Boolean> ()

    init {
        isExistSnapShot=repository.isExistSnapShot
    }

    fun getHistorySales(idUser:String): LiveData<MutableList<Sales>> {
        val mutabledata = MutableLiveData<MutableList<Sales>>()
        viewModelScope.launch {
            repository.getHistorySales(idUser).observeForever{
                mutabledata.value = it
            }
        }
        return mutabledata
    }
}