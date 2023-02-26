package com.tikonsil.tikonsil509.presentation.splashScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import kotlinx.coroutines.launch

class SplashScreenViewModel:ViewModel() {
    val error = MutableLiveData<Boolean>()
    val success = MutableLiveData<Boolean>()

    fun getFirebaseApi() {
        viewModelScope.launch {
            val apis = FirebaseApi.getFSApis()

            if (apis.base_url_tikonsil .isEmpty() && apis.key.isEmpty()
                && apis.fcm.base_url_fcm.isEmpty()) {
                error.value = true
                return@launch
            }

            success.value = true
        }
    }
}