package com.tikonsil.tikonsil509.presentation.version

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.domain.model.VersionsResponse
import com.tikonsil.tikonsil509.domain.repository.version.VersionCodeRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class VersionCodeViewModel:ViewModel() {
    private val repository:VersionCodeRepository = VersionCodeRepository()
    private val _versionCode:MutableLiveData<Response<VersionsResponse>> by lazy { MutableLiveData() }
    val versionCode:LiveData<Response<VersionsResponse>> = _versionCode

    fun getVersionCode(){
        viewModelScope.launch {
            val result = repository.getVersionCode()
            _versionCode.postValue(result)
        }
    }
}